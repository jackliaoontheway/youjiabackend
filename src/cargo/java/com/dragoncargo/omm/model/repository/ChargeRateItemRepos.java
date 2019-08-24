package com.dragoncargo.omm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.omm.model.ChargeRateItem;

public interface ChargeRateItemRepos extends JpaRepository<ChargeRateItem, Integer>
{
}
