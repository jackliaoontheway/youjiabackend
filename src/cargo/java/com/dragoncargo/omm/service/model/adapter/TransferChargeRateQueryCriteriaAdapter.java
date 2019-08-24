package com.dragoncargo.omm.service.model.adapter;

import com.dragoncargo.omm.service.model.QueryCriteria;
import com.dragoncargo.omm.service.model.TransferChargeRateQueryCriteria;

public class TransferChargeRateQueryCriteriaAdapter extends TransferChargeRateQueryCriteria
{

    public TransferChargeRateQueryCriteriaAdapter(QueryCriteria queryCriteria)
    {

        this.setArrivingAirPortCode(queryCriteria.getArrivingAirPortCode());
        this.setAviationCompanyCode(queryCriteria.getAviationCompanyCode());
        this.setTransferToCityCode(queryCriteria.getTransferToCityCode());

    }

}
