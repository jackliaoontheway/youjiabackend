package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.AirPortInfo;

public interface AirPortInfoRepos extends JpaRepository<AirPortInfo, Integer>
{
}
