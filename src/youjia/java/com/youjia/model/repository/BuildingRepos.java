package com.youjia.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.youjia.model.Building;

public interface BuildingRepos extends JpaRepository<Building, Integer>
{
}
