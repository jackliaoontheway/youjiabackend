package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragoncargo.omm.model.ChargeRateItemCategory;

public interface ChargeRateItemCategoryRepos extends JpaRepository<ChargeRateItemCategory, Integer>
{

    ChargeRateItemCategory findFirstByCode(String chargeRateCategoryCode);

    List<ChargeRateItemCategory> findByMultipleQueryAvailable(boolean b);
}
