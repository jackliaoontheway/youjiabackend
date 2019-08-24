package com.dragoncargo.omm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.AviationCompanyLocalChargeRate;
import com.dragoncargo.omm.model.service.AviationCompanyLocalChargeRateService;
import com.dragoncargo.omm.service.model.AviationCompanyLocalChargeRateData;
import com.dragoncargo.omm.service.model.AviationCompanyLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.adapter.AviationCompanyLocalChargeRateDataAdapter;
import com.dragoncargo.omm.service.model.adapter.AviationCompanyLocalChargeRateQueryCriteriaAdapter;

@Service("AviationCompanyLocalChargeRateQueryService")
public class AviationCompanyLocalChargeRateQueryServiceImpl extends
        AbstractChargeRateQueryServiceTemplate<AviationCompanyLocalChargeRateQueryCriteria,
                AviationCompanyLocalChargeRateData, AviationCompanyLocalChargeRate,
                AviationCompanyLocalChargeRateService>
{

    @Override
    protected AviationCompanyLocalChargeRateData
            convertToChargeRateData(AviationCompanyLocalChargeRate chargeRateConfiguration)
    {
        return new AviationCompanyLocalChargeRateDataAdapter(chargeRateConfiguration);
    }

    @Override
    protected List<AviationCompanyLocalChargeRate> filterByCriteria(
            List<AviationCompanyLocalChargeRate> chargeRateConfigurationList,
            AviationCompanyLocalChargeRateQueryCriteria criteria)
    {
        return chargeRateConfigurationList;
    }

    @Override
    protected AviationCompanyLocalChargeRateQueryCriteria convertCriteria(QueryCriteria queryCriteria)
    {
        return new AviationCompanyLocalChargeRateQueryCriteriaAdapter(queryCriteria);
    }

}
