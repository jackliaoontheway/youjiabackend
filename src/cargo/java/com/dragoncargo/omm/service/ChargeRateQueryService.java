package com.dragoncargo.omm.service;

import com.dragoncargo.omm.service.model.BaseChargeRateData;
import com.dragoncargo.omm.service.model.BaseCriteria;
import com.dragoncargo.omm.service.model.QueryChargeRateResponse;
import com.dragoncargo.omm.service.model.QueryCriteria;

public interface ChargeRateQueryService<T extends BaseCriteria, R extends BaseChargeRateData>
{
    QueryChargeRateResponse<R> query(QueryCriteria queryCriteria, String chargeRateItemCategoryCode);
}
