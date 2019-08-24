package com.dragoncargo.omm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dragoncargo.general.model.AirPortInfo;
import com.dragoncargo.general.model.ChargeUnit;
import com.dragoncargo.general.model.WeightType;
import com.dragoncargo.omm.model.AbstractChargeRate;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.ChargeRateItemCategory;
import com.dragoncargo.omm.model.ChargeRateStatus;
import com.dragoncargo.omm.model.ChargeRateType;
import com.dragoncargo.omm.model.service.AbstractChargeRateService;
import com.dragoncargo.omm.model.service.ChargeRateItemCategoryService;
import com.dragoncargo.omm.service.ChargeRateQueryService;
import com.dragoncargo.omm.service.model.BaseChargeRateData;
import com.dragoncargo.omm.service.model.BaseCriteria;
import com.dragoncargo.omm.service.model.FeeData;
import com.dragoncargo.omm.service.model.QueryChargeRateResponse;
import com.dragoncargo.omm.service.model.QueryCriteria;

import lombok.Setter;

public abstract class AbstractChargeRateQueryServiceTemplate<T extends BaseCriteria, R extends BaseChargeRateData,
        M extends AbstractChargeRate, S extends AbstractChargeRateService<M>> implements ChargeRateQueryService<T, R>
{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected S service;

    @Autowired
    private @Setter ChargeRateItemCategoryService chargeRateItemCategoryService;

    protected abstract T convertCriteria(QueryCriteria queryCriteria);

    protected abstract R convertToChargeRateData(M configuration);

    protected abstract List<M> filterByCriteria(List<M> chargeRateConfigurationList, T criteria);

    @Override
    public final QueryChargeRateResponse<R> query(QueryCriteria queryCriteria, String chargeRateItemCategoryCode)
    {
        QueryChargeRateResponse<R> response = new QueryChargeRateResponse<>();

        List<ChargeRateItem> items = findChargeRateItemByCategoryCode(chargeRateItemCategoryCode);

        if (items == null || items.size() == 0)
        {
            response.setErrorMsg("Can not find charge rate item by category :" + chargeRateItemCategoryCode);
            return response;
        }

        T criteria = convertCriteria(queryCriteria);
        setBaseCriteria(criteria, queryCriteria);

        List<R> chargeRateData = new ArrayList<>();
        for (ChargeRateItem item : items)
        {
            List<M> list = service.findByChargeRateItem(item);
            if (list == null || list.size() == 0)
            {
                continue;
            }

            List<M> chargeRateConfigurationList = fetchAvailableConfiguration(list, criteria);
            if (chargeRateConfigurationList != null && chargeRateConfigurationList.size() > 0)
            {
                chargeRateConfigurationList = filterByCriteria(chargeRateConfigurationList, criteria);

                for (M chargeRateConfiguration : chargeRateConfigurationList)
                {
                    R result = convertToChargeRateData(chargeRateConfiguration);

                    setBaseChargeRateData(result, chargeRateConfiguration);

                    chargeRateData.add(result);
                }
            }
        }

        response.setChargeRateData(chargeRateData);

        return response;
    }

    private void setBaseCriteria(T criteria, QueryCriteria queryCriteria)
    {
        criteria.setChargeRateTypeList(queryCriteria.getChargeRateTypeList());
        criteria.setDepartAirPortCode(queryCriteria.getDepartAirPortCode());
        criteria.setToCityCode(queryCriteria.getToCityCode());
    }

    private List<ChargeRateItem> findChargeRateItemByCategoryCode(String chargeRateCategoryCode)
    {
        ChargeRateItemCategory chargeRateCategory = chargeRateItemCategoryService.findByCode(chargeRateCategoryCode);
        return (chargeRateCategory == null ? null : chargeRateCategory.getChargeRateItemList());
    }

    private List<M> fetchAvailableConfiguration(List<M> list, T criteria)
    {
        List<M> availableConfigurationList = new ArrayList<>();
        Date now = new Date();
        for (M configuration : list)
        {
            if (criteria.getDepartAirPortCode() != null && configuration.getAirPortInfo() != null
                    && criteria.getDepartAirPortCode().equals(configuration.getAirPortInfo().getCode()))
            {
                if (now.after(configuration.getEffetiveDate()) && now.before(configuration.getExpiredDate()))
                {
                    if (ChargeRateStatus.ENABLED.name().equals(configuration.getStatus()))
                    {
                        if (configuration.isEstimateAvailable())
                        {
                            if (criteria.getChargeRateTypeList() != null && configuration.getChargeRateType() != null
                                    && criteria.getChargeRateTypeList()
                                            .contains(configuration.getChargeRateType().getCode()))
                            {
                                availableConfigurationList.add(configuration);
                            }
                        }
                    }
                }
            }
        }
        return availableConfigurationList;
    }

    private void setBaseChargeRateData(R data, M chargeRateConfiguration)
    {
        AirPortInfo airPortInfo = chargeRateConfiguration.getAirPortInfo();
        if (airPortInfo != null)
        {
            data.setAirPortCode(airPortInfo.getCode());
            data.setAirPortName(airPortInfo.getName());
        }

        ChargeRateType chargeRateType = chargeRateConfiguration.getChargeRateType();
        if (chargeRateType != null)
        {
            data.setChargeRateTypeCode(chargeRateType.getCode());
            data.setChargeRateTypeName(chargeRateType.getName());
        }

        ChargeRateItem chargeRateItem = chargeRateConfiguration.getChargeRateItem();
        if (chargeRateItem != null)
        {
            data.setChargeRateCode(chargeRateItem.getCode());
            data.setChargeRateName(chargeRateItem.getName());
        }

        ChargeUnit chargeUnit = chargeRateConfiguration.getChargeUnit();
        if (chargeUnit != null)
        {
            data.setChargeUnitCode(chargeUnit.getCode());
            data.setChargeUnitName(chargeUnit.getName());
        }

        WeightType weightType = chargeRateConfiguration.getWeightType();
        if (weightType != null)
        {
            data.setWeightTypeCode(weightType.getCode());
            data.setWeightTypeName(weightType.getName());
        }

        data.setEffetiveDate(chargeRateConfiguration.getEffetiveDate());
        data.setExpiredDate(chargeRateConfiguration.getExpiredDate());
        data.setMaximumCharge(new FeeData(chargeRateConfiguration.getMaximumCharge()));
        data.setMinimumCharge(new FeeData(chargeRateConfiguration.getMinimumCharge()));
        data.setUnitPrice(new FeeData(chargeRateConfiguration.getUnitPrice()));
        data.setRemark(chargeRateConfiguration.getRemark());
    }

}
