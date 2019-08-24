package com.dragoncargo.omm.model.service;

import com.polarj.model.service.EntityService;

import java.util.List;

import com.dragoncargo.omm.model.ChargeRateItemCategory;

public interface ChargeRateItemCategoryService extends EntityService<ChargeRateItemCategory, Integer>
{

    ChargeRateItemCategory findByCode(String chargeRateCategoryCode);

    List<ChargeRateItemCategory> findCategoryForMultipleQuery();
}
