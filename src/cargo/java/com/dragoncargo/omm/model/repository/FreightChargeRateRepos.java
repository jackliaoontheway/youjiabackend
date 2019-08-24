package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.FreightChargeRate;

public interface FreightChargeRateRepos extends JpaRepository<FreightChargeRate, Integer>
{

    List<FreightChargeRate> findByChargeRateItem(ChargeRateItem item);

}
