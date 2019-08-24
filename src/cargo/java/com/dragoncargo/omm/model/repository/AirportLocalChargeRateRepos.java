package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.omm.model.AirportLocalChargeRate;
import com.dragoncargo.omm.model.ChargeRateItem;

public interface AirportLocalChargeRateRepos extends JpaRepository<AirportLocalChargeRate, Integer>
{

    List<AirportLocalChargeRate> findByChargeRateItem(ChargeRateItem item);
}
