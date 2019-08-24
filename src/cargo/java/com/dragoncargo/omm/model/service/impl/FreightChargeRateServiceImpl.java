package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.FreightChargeRate;
import com.dragoncargo.omm.model.repository.FreightChargeRateRepos;
import com.dragoncargo.omm.model.service.FreightChargeRateService;

@Service
public class FreightChargeRateServiceImpl extends AbstractChargeRateServiceImpl<FreightChargeRate>
        implements FreightChargeRateService
{

    @Override
    public List<FreightChargeRate> findByChargeRateItem(ChargeRateItem item)
    {
        FreightChargeRateRepos repos = (FreightChargeRateRepos) getRepos();

        return repos.findByChargeRateItem(item);
    }
    
}
