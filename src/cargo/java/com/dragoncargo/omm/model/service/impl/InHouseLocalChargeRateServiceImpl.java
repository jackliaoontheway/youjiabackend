package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.InHouseLocalChargeRate;
import com.dragoncargo.omm.model.repository.InHouseLocalChargeRateRepos;
import com.dragoncargo.omm.model.service.InHouseLocalChargeRateService;
import com.polarj.model.service.impl.EntityServiceImpl;

@Service
public class InHouseLocalChargeRateServiceImpl extends EntityServiceImpl<InHouseLocalChargeRate, Integer>
        implements InHouseLocalChargeRateService
{

    @Override
    public List<InHouseLocalChargeRate> findByChargeRateItem(ChargeRateItem item)
    {
        InHouseLocalChargeRateRepos repos = (InHouseLocalChargeRateRepos) getRepos();

        return repos.findByChargeRateItem(item);
    }
}
