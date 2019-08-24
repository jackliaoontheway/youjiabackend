package com.dragoncargo.omm.service.model.adapter;

import com.dragoncargo.omm.service.model.AviationCompanyLocalChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;

public class AviationCompanyLocalChargeRateQueryCriteriaAdapter extends AviationCompanyLocalChargeRateQueryCriteria
{

    public AviationCompanyLocalChargeRateQueryCriteriaAdapter(QueryCriteria queryCriteria)
    {
        this.setAviationCompanyCode(queryCriteria.getAviationCompanyCode());
    }

}
