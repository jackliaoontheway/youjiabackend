package com.dragoncargo.omm.service;

import com.dragoncargo.omm.service.model.QueryChargeRateComponentResponse;
import com.dragoncargo.omm.service.model.QueryCriteria;

public interface ChargeRateQueryFacade
{

    QueryChargeRateComponentResponse multipleQuery(QueryCriteria queryCriteria);

    QueryChargeRateComponentResponse simpleQuery(QueryCriteria queryCriteria);

}
