package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.AirportWarehouseLocalChargeRate;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.repository.AirportWarehouseLocalChargeRateRepos;
import com.dragoncargo.omm.model.service.AirportWarehouseLocalChargeRateService;

@Service
public class AirportWarehouseLocalChargeRateServiceImpl extends AbstractChargeRateServiceImpl<AirportWarehouseLocalChargeRate>
        implements AirportWarehouseLocalChargeRateService
{

    @Override
    public List<AirportWarehouseLocalChargeRate> findByChargeRateItem(ChargeRateItem item)
    {
        AirportWarehouseLocalChargeRateRepos repos = (AirportWarehouseLocalChargeRateRepos) getRepos();

        return repos.findByChargeRateItem(item);
    }
}
