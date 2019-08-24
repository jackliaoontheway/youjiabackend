package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.InHouseLocalChargeRate;

public interface InHouseLocalChargeRateRepos extends JpaRepository<InHouseLocalChargeRate, Integer>
{

    List<InHouseLocalChargeRate> findByChargeRateItem(ChargeRateItem item);
}
