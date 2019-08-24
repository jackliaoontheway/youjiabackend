package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.AirportLocalChargeRate;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.repository.AirportLocalChargeRateRepos;
import com.dragoncargo.omm.model.service.AirportLocalChargeRateService;

@Service
public class AirportLocalChargeRateServiceImpl extends AbstractChargeRateServiceImpl<AirportLocalChargeRate>
        implements AirportLocalChargeRateService
{

    @Override
    public List<AirportLocalChargeRate> findByChargeRateItem(ChargeRateItem item)
    {
        AirportLocalChargeRateRepos repos = (AirportLocalChargeRateRepos) getRepos();

        return repos.findByChargeRateItem(item);
    }
}
