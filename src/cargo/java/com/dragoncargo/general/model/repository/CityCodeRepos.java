package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.CityCode;

public interface CityCodeRepos extends JpaRepository<CityCode, Integer>
{
}
