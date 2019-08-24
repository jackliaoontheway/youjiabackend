package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.omm.model.AviationCompanyLocalChargeRate;
import com.dragoncargo.omm.model.ChargeRateItem;

public interface AviationCompanyLocalChargeRateRepos extends JpaRepository<AviationCompanyLocalChargeRate, Integer>
{

    List<AviationCompanyLocalChargeRate> findByChargeRateItem(ChargeRateItem item);
}
