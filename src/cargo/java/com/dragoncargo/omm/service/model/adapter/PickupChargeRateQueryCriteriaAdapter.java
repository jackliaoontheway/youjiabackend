package com.dragoncargo.omm.service.model.adapter;

import com.dragoncargo.omm.service.model.PickupChargeRateQueryCriteria;
import com.dragoncargo.omm.service.model.QueryCriteria;

public class PickupChargeRateQueryCriteriaAdapter extends PickupChargeRateQueryCriteria
{

    public PickupChargeRateQueryCriteriaAdapter(QueryCriteria queryCriteria)
    {
        this.setPickupLocation(queryCriteria.getPickupLocation());
    }

}
