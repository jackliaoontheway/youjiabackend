package com.dragoncargo.omm.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dragoncargo.general.model.AirPortInfo;
import com.dragoncargo.general.model.Airline;
import com.dragoncargo.general.model.CityCode;
import com.dragoncargo.general.model.Weight;
import com.dragoncargo.general.model.service.AirPortInfoService;
import com.dragoncargo.omm.model.FreightChargeRate;
import com.dragoncargo.omm.model.NavicationInfo;
import com.dragoncargo.omm.model.TransferChargeRate;
import com.dragoncargo.omm.model.WeightSpec;
import com.dragoncargo.omm.model.service.FreightChargeRateService;
import com.dragoncargo.omm.model.service.TransferChargeRateService;
import com.dragoncargo.omm.model.service.WeightSpecService;
import com.dragoncargo.omm.service.model.FreightChargeRateData;
import com.dragoncargo.omm.service.model.FreightChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.VolumnData;
import com.dragoncargo.omm.service.model.WeightData;
import com.dragoncargo.omm.service.model.adapter.FreightChargeRateDataAdapter;
import com.dragoncargo.omm.service.model.adapter.FreightChargeRateQueryCriteriaAdapter;
import com.polarj.common.CommonConstant;

@Service("FreightRateQueryService")
public class FreightChargeRateQueryServiceImpl extends AbstractChargeRateQueryServiceTemplate<
        FreightChargeRateQueryCriteria, FreightChargeRateData, FreightChargeRate, FreightChargeRateService>
{

    @Autowired
    private AirPortInfoService airPortInfoService;

    @Autowired
    private WeightSpecService weightSpecService;

    @Autowired
    private TransferChargeRateService transferChargeRateService;

    @Override
    public FreightChargeRateData convertToChargeRateData(FreightChargeRate configuration)
    {
        return new FreightChargeRateDataAdapter(configuration);
    }

    @Override
    protected List<FreightChargeRate> filterByCriteria(List<FreightChargeRate> chargeRateConfigurationList,
            FreightChargeRateQueryCriteria criteria)
    {
        List<FreightChargeRate> resultList = new ArrayList<>();

        if (chargeRateConfigurationList == null || chargeRateConfigurationList.size() == 0)
        {
            return resultList;
        }

        List<AirPortInfo> airPortInfoList = airPortInfoService.list(CommonConstant.defaultSystemLanguage);
        if (airPortInfoList == null || airPortInfoList.size() == 0)
        {
            return resultList;
        }

        String toCity = criteria.getToCityCode();
        if (StringUtils.isEmpty(toCity))
        {
            return resultList;
        }

        boolean toCityCodeMatchedArrivingCityCode = false;
        for (AirPortInfo airPortInfo : airPortInfoList)
        {
            if (airPortInfo.getAddressInfo() == null)
            {
                continue;
            }
            if (toCity.equals(airPortInfo.getAddressInfo().getCity()))
            {
                toCityCodeMatchedArrivingCityCode = true;
            }
        }

        Set<String> arrivingAirportCodeSet = new HashSet<>();
        if (!toCityCodeMatchedArrivingCityCode)
        {
            arrivingAirportCodeSet = findArrivingAirportForTransfer(toCity);
            if (arrivingAirportCodeSet == null || arrivingAirportCodeSet.size() == 0)
            {
                logger.info("Can't find transfer airport for this address" + toCity);
                return resultList;
            }
        }

        Set<String> chargeWeightSpecSet = getWeightSpec(criteria);

        for (FreightChargeRate chargeRate : chargeRateConfigurationList)
        {
            if (!checkDepartAirPortCode(criteria, chargeRate))
            {
                continue;
            }

            if (toCityCodeMatchedArrivingCityCode)
            {
                // 不需要转运 使用ArrivingAddress toCityCode 判断
                if (!checkArrivingAddress(criteria, chargeRate))
                {
                    continue;
                }
            }
            else
            {
                // 需要转运 使用ArrivingAirPortCode 判断
                if (!checkArrivingAirPortCode(arrivingAirportCodeSet, chargeRate))
                {
                    continue;
                }
                chargeRate.setTransferNeeded(true);
            }

            if (!checkWeight(chargeWeightSpecSet, chargeRate))
            {
                continue;
            }

            resultList.add(chargeRate);

        }
        return resultList;
    }

    private Set<String> getWeightSpec(FreightChargeRateQueryCriteria criteria)
    {
        Set<String> chargeWeightSpecSet = new HashSet<>();
        List<WeightSpec> weightSpecList = weightSpecService.list(CommonConstant.defaultSystemLanguage);
        WeightData grossWeightData = criteria.getWeight();
        if (grossWeightData == null || grossWeightData.getUnit() == null || grossWeightData.getValue() == null)
        {
            for (WeightSpec weightSpec : weightSpecList)
            {
                chargeWeightSpecSet.add(weightSpec.getCode());
            }
            return chargeWeightSpecSet;
        }

        BigDecimal grossWeight = grossWeightData.getValue();
        BigDecimal chargeWeight = null;
        VolumnData volumnData = criteria.getVolumnData();
        if (volumnData != null)
        {
            BigDecimal volumnWeight = volumnData.getValue().multiply(new BigDecimal(167)).setScale(2);
            if (grossWeight.compareTo(volumnWeight) <= 0)
            {
                chargeWeight = volumnWeight;
            }
        }

        // 重货 使用毛重 和 毛重 + 1rank
        // 泡货 毛重rank 到 体积重rank+1

        WeightSpec selectedGrossWeightSpec = null;
        WeightSpec selectedChargeWeightSpec = null;
        for (WeightSpec weightSpec : weightSpecList)
        {
            Weight minimumRangeWeight = weightSpec.getMinimumRangeWeight();
            Weight maximumRangeWeight = weightSpec.getMaximumRangeWeight();

            if (minimumRangeWeight.getUnit() == null || minimumRangeWeight.getValue() == null)
            {
                continue;
            }
            if (maximumRangeWeight.getUnit() == null || maximumRangeWeight.getValue() == null)
            {
                continue;
            }

            if (grossWeightData.getUnit().equals(minimumRangeWeight.getUnit()))
            {
                if (grossWeight.compareTo(minimumRangeWeight.getValue()) > 0
                        && grossWeight.compareTo(maximumRangeWeight.getValue()) <= 0)
                {
                    selectedGrossWeightSpec = weightSpec;
                }

                if (chargeWeight != null && chargeWeight.compareTo(minimumRangeWeight.getValue()) > 0
                        && chargeWeight.compareTo(maximumRangeWeight.getValue()) <= 0)
                {
                    selectedChargeWeightSpec = weightSpec;
                }
            }
        }

        if (selectedGrossWeightSpec != null)
        {
            Integer rank = selectedGrossWeightSpec.getWeightRank();
            rank = (rank == null ? 0 : rank);

            Integer maxRank = rank;
            if (selectedChargeWeightSpec != null)
            {
                maxRank = selectedChargeWeightSpec.getWeightRank();
                maxRank = (maxRank == null ? 0 : maxRank);
            }

            boolean queryAdjacentRange = criteria.isQueryAdjacentRange();
            if (queryAdjacentRange)
            {
                maxRank += 1;
            }

            for (WeightSpec weightSpec : weightSpecList)
            {
                Integer weightRank = weightSpec.getWeightRank();
                if (weightRank == null)
                {
                    continue;
                }
                if (rank <= weightRank && weightRank <= maxRank)
                {
                    chargeWeightSpecSet.add(weightSpec.getCode());
                }
            }
        }

        return chargeWeightSpecSet;
    }

    private Set<String> findArrivingAirportForTransfer(String toCity)
    {
        Set<String> arrivingAirportCodeSet = new HashSet<>();
        List<TransferChargeRate> transferChargeRateList =
                transferChargeRateService.list(CommonConstant.defaultSystemLanguage);
        if (transferChargeRateList == null || transferChargeRateList.size() == 0)
        {
            return arrivingAirportCodeSet;
        }

        for (TransferChargeRate transfer : transferChargeRateList)
        {
            List<CityCode> transferToCityList = transfer.getTransferToCityList();
            if (transferToCityList == null || transferToCityList.size() == 0)
            {
                continue;
            }
            boolean toCityCodeMatchedTransferCityCode = false;
            for (CityCode cityCode : transferToCityList)
            {
                if (toCity.equals(cityCode.getCode()))
                {
                    toCityCodeMatchedTransferCityCode = true;
                }
            }
            if (toCityCodeMatchedTransferCityCode)
            {
                if (transfer.getArrivingAirPort() != null)
                {
                    arrivingAirportCodeSet.add(transfer.getArrivingAirPort().getCode());
                }
            }
        }
        return arrivingAirportCodeSet;
    }

    private boolean checkArrivingAirPortCode(Set<String> arrivingAirportCodeSet, FreightChargeRate chargeRate)
    {
        if (chargeRate == null)
        {
            return false;
        }
        if (arrivingAirportCodeSet == null || arrivingAirportCodeSet.size() == 0)
        {
            return false;
        }

        NavicationInfo navicationInfo = chargeRate.getNavicationInfo();
        if (navicationInfo == null)
        {
            return false;
        }

        Airline airline = navicationInfo.getAirline();
        if (airline == null)
        {
            return false;
        }

        AirPortInfo arrivingAirPort = airline.getArrivingAirPort();
        if (arrivingAirPort == null)
        {
            return false;
        }

        return arrivingAirportCodeSet.contains(arrivingAirPort.getCode());
    }

    private boolean checkArrivingAddress(FreightChargeRateQueryCriteria criteria, FreightChargeRate chargeRate)
    {
        if (chargeRate == null)
        {
            return false;
        }
        if (criteria == null)
        {
            return false;
        }

        String toCity = criteria.getToCityCode();
        if (StringUtils.isEmpty(toCity))
        {
            return false;
        }

        NavicationInfo navicationInfo = chargeRate.getNavicationInfo();
        if (navicationInfo == null)
        {
            return false;
        }

        Airline airline = navicationInfo.getAirline();
        if (airline == null)
        {
            return false;
        }

        AirPortInfo arrivingAirPort = airline.getArrivingAirPort();
        if (arrivingAirPort == null)
        {
            return false;
        }
        if (arrivingAirPort.getAddressInfo() == null)
        {
            return false;
        }

        return (toCity.equals(arrivingAirPort.getAddressInfo().getCity()));
    }

    private boolean checkDepartAirPortCode(FreightChargeRateQueryCriteria criteria, FreightChargeRate chargeRate)
    {
        if (chargeRate == null)
        {
            return false;
        }
        if (criteria == null)
        {
            return false;
        }
        String departAirPortCode = criteria.getDepartAirPortCode();
        if (StringUtils.isEmpty(departAirPortCode))
        {
            return false;
        }

        NavicationInfo navicationInfo = chargeRate.getNavicationInfo();
        if (navicationInfo == null)
        {
            return false;
        }
        Airline airline = navicationInfo.getAirline();
        if (airline == null)
        {
            return false;
        }
        AirPortInfo departAirPort = airline.getDepartAirPort();
        if (departAirPort == null)
        {
            return false;
        }

        return (departAirPortCode.equals(departAirPort.getCode()));
    }

    private boolean checkWeight(Set<String> chargeWeightSpecSet, FreightChargeRate chargeRate)
    {
        WeightSpec weightSpec = chargeRate.getWeightSpec();

        if (weightSpec == null || chargeWeightSpecSet == null)
        {
            return false;
        }

        return chargeWeightSpecSet.contains(weightSpec.getCode());
    }

    @Override
    protected FreightChargeRateQueryCriteria convertCriteria(QueryCriteria queryCriteria)
    {
        return new FreightChargeRateQueryCriteriaAdapter(queryCriteria);
    }

}
