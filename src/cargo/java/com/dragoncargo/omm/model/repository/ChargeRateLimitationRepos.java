package com.dragoncargo.omm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.omm.model.ChargeRateLimitation;

public interface ChargeRateLimitationRepos extends JpaRepository<ChargeRateLimitation, Integer>
{
}
