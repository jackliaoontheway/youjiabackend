package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.FreightSubChargeRate;
import com.dragoncargo.omm.model.repository.FreightSubChargeRateRepos;
import com.dragoncargo.omm.model.service.FreightSubChargeRateService;

@Service
public class FreightSubChargeRateServiceImpl extends AbstractChargeRateServiceImpl<FreightSubChargeRate>
        implements FreightSubChargeRateService
{

    @Override
    public List<FreightSubChargeRate> findByChargeRateItem(ChargeRateItem item)
    {
        FreightSubChargeRateRepos repos = (FreightSubChargeRateRepos) getRepos();

        return repos.findByChargeRateItem(item);
    }
}
