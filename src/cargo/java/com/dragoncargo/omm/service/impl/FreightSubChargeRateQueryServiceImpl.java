package com.dragoncargo.omm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.FreightSubChargeRate;
import com.dragoncargo.omm.model.service.FreightSubChargeRateService;
import com.dragoncargo.omm.service.model.FreightSubChargeRateData;
import com.dragoncargo.omm.service.model.FreightSubChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.adapter.FreightSubChargeRateDataAdapter;
import com.dragoncargo.omm.service.model.adapter.FreightSubChargeRateQueryCriteriaAdapter;

@Service("FreightSubChargeRateQueryService")
public class FreightSubChargeRateQueryServiceImpl extends AbstractChargeRateQueryServiceTemplate<
        FreightSubChargeRateQueryCriteria, FreightSubChargeRateData, FreightSubChargeRate, FreightSubChargeRateService>
{

    @Override
    protected FreightSubChargeRateData convertToChargeRateData(FreightSubChargeRate configuration)
    {
        return new FreightSubChargeRateDataAdapter(configuration);
    }

    @Override
    protected List<FreightSubChargeRate> filterByCriteria(List<FreightSubChargeRate> chargeRateConfigurationList,
            FreightSubChargeRateQueryCriteria criteria)
    {
        return chargeRateConfigurationList;
    }

    @Override
    protected FreightSubChargeRateQueryCriteria convertCriteria(QueryCriteria queryCriteria)
    {
        return new FreightSubChargeRateQueryCriteriaAdapter(queryCriteria);
    }

}
