package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.ChargeUnit;

import java.util.List;

public interface ChargeUnitRepos extends JpaRepository<ChargeUnit, Integer>
{
}
