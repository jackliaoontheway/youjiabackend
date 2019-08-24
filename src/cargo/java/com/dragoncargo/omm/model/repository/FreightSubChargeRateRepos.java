package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.FreightSubChargeRate;

public interface FreightSubChargeRateRepos extends JpaRepository<FreightSubChargeRate, Integer>
{

    List<FreightSubChargeRate> findByChargeRateItem(ChargeRateItem item);
}
