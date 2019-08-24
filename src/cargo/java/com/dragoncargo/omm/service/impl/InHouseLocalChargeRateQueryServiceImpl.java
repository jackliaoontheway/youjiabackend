package com.dragoncargo.omm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.InHouseLocalChargeRate;
import com.dragoncargo.omm.model.service.InHouseLocalChargeRateService;
import com.dragoncargo.omm.service.model.InHouseLocalChargeRateData;
import com.dragoncargo.omm.service.model.InHouseLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.adapter.InHouseLocalChargeRateDataAdapter;
import com.dragoncargo.omm.service.model.adapter.InHouseLocalChargeRateQueryCriteriaAdapter;

@Service("InHouseLocalChargeRateQueryService")
public class InHouseLocalChargeRateQueryServiceImpl
        extends AbstractChargeRateQueryServiceTemplate<InHouseLocalChargeRateQueryCriteria, InHouseLocalChargeRateData,
        InHouseLocalChargeRate, InHouseLocalChargeRateService>
{

    @Override
    protected InHouseLocalChargeRateData convertToChargeRateData(InHouseLocalChargeRate configuration)
    {
        return new InHouseLocalChargeRateDataAdapter(configuration);
    }

    @Override
    protected List<InHouseLocalChargeRate> filterByCriteria(List<InHouseLocalChargeRate> chargeRateConfigurationList,
            InHouseLocalChargeRateQueryCriteria criteria)
    {
        return chargeRateConfigurationList;
    }

    @Override
    protected InHouseLocalChargeRateQueryCriteria convertCriteria(QueryCriteria queryCriteria)
    {
        return new InHouseLocalChargeRateQueryCriteriaAdapter(queryCriteria);
    }
}
