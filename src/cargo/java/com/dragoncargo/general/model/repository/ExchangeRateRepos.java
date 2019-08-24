package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.ExchangeRate;

public interface ExchangeRateRepos extends JpaRepository<ExchangeRate, Integer>
{
}
