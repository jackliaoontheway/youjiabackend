package com.dragoncargo.omm.service.model.adapter;

import com.dragoncargo.omm.service.model.FreightChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;

public class FreightChargeRateQueryCriteriaAdapter extends FreightChargeRateQueryCriteria
{

    public FreightChargeRateQueryCriteriaAdapter(QueryCriteria queryCriteria)
    {
        this.setWeight(queryCriteria.getWeight());
        this.setVolumnData(queryCriteria.getVolumnData());
        this.setQueryAdjacentRange(queryCriteria.isQueryAdjacentRange());
    }

}
