package com.dragoncargo.omm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.omm.model.FlightInfo;

public interface FlightInfoRepos extends JpaRepository<FlightInfo, Integer>
{
}
