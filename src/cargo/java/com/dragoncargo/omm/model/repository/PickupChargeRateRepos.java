package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.PickupChargeRate;

public interface PickupChargeRateRepos extends JpaRepository<PickupChargeRate, Integer>
{

    List<PickupChargeRate> findByChargeRateItem(ChargeRateItem item);
}
