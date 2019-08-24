package com.dragoncargo.omm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dragoncargo.general.model.AirPortInfo;
import com.dragoncargo.general.model.AviationCompany;
import com.dragoncargo.general.model.CityCode;
import com.dragoncargo.omm.model.TransferChargeRate;
import com.dragoncargo.omm.model.service.TransferChargeRateService;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.TransferChargeRateData;
import com.dragoncargo.omm.service.model.TransferChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.adapter.TransferChargeRateDataAdapter;
import com.dragoncargo.omm.service.model.adapter.TransferChargeRateQueryCriteriaAdapter;

@Service("TransferChargeRateQueryService")
public class TransferChargeRateQueryServiceImpl extends AbstractChargeRateQueryServiceTemplate<
        TransferChargeRateQueryCriteria, TransferChargeRateData, TransferChargeRate, TransferChargeRateService>
{

    @Override
    protected TransferChargeRateData convertToChargeRateData(TransferChargeRate configuration)
    {
        return new TransferChargeRateDataAdapter(configuration);
    }

    @Override
    protected List<TransferChargeRate> filterByCriteria(List<TransferChargeRate> chargeRateConfigurationList,
            TransferChargeRateQueryCriteria criteria)
    {

        List<TransferChargeRate> resultList = new ArrayList<>();
        String aviationCompanyCode = criteria.getAviationCompanyCode();
        if (StringUtils.isEmpty(aviationCompanyCode))
        {
            return resultList;
        }

        String arrivingAirPortCode = criteria.getArrivingAirPortCode();
        if (StringUtils.isEmpty(arrivingAirPortCode))
        {
            return resultList;
        }

        for (TransferChargeRate chargeRate : chargeRateConfigurationList)
        {
            AirPortInfo arrivingAirPort = chargeRate.getArrivingAirPort();
            if (arrivingAirPort == null)
            {
                continue;
            }
            AviationCompany aviationCompany = chargeRate.getAviationCompany();
            if (aviationCompany == null)
            {
                continue;
            }

            if (!arrivingAirPortCode.equals(arrivingAirPort.getCode()))
            {
                continue;
            }

            if (!aviationCompanyCode.equals(aviationCompany.getCode()))
            {
                continue;
            }

            List<CityCode> transferToCityList = chargeRate.getTransferToCityList();
            for (CityCode cityCode : transferToCityList)
            {
                if (criteria.getTransferToCityCode() != null
                        && criteria.getTransferToCityCode().equals(cityCode.getCode()))
                {
                    resultList.add(chargeRate);
                }
            }
        }

        return resultList;
    }

    @Override
    protected TransferChargeRateQueryCriteria convertCriteria(QueryCriteria queryCriteria)
    {
        return new TransferChargeRateQueryCriteriaAdapter(queryCriteria);
    }

}
