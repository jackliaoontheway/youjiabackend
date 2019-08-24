package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.omm.model.AirportWarehouseLocalChargeRate;
import com.dragoncargo.omm.model.ChargeRateItem;

public interface AirportWarehouseLocalChargeRateRepos extends JpaRepository<AirportWarehouseLocalChargeRate, Integer>
{

    List<AirportWarehouseLocalChargeRate> findByChargeRateItem(ChargeRateItem item);
}
