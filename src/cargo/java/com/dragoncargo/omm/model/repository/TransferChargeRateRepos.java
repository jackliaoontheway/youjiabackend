package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.TransferChargeRate;

public interface TransferChargeRateRepos extends JpaRepository<TransferChargeRate, Integer>
{

    List<TransferChargeRate> findByChargeRateItem(ChargeRateItem item);
}
