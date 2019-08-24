package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.PickupChargeRate;
import com.dragoncargo.omm.model.repository.PickupChargeRateRepos;
import com.dragoncargo.omm.model.service.PickupChargeRateService;

@Service
public class PickupChargeRateServiceImpl extends AbstractChargeRateServiceImpl<PickupChargeRate>
        implements PickupChargeRateService
{

    @Override
    public List<PickupChargeRate> findByChargeRateItem(ChargeRateItem item)
    {
        PickupChargeRateRepos repos = (PickupChargeRateRepos) getRepos();

        return repos.findByChargeRateItem(item);
    }
}
