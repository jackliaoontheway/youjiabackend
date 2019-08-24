package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.AirportWarehouse;

public interface AirportWarehouseRepos extends JpaRepository<AirportWarehouse, Integer>
{
    AirportWarehouse findByCode(String code);
}
