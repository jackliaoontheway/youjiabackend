package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.TransferChargeRate;
import com.dragoncargo.omm.model.repository.TransferChargeRateRepos;
import com.dragoncargo.omm.model.service.TransferChargeRateService;

@Service
public class TransferChargeRateServiceImpl extends AbstractChargeRateServiceImpl<TransferChargeRate>
        implements TransferChargeRateService
{

    @Override
    public List<TransferChargeRate> findByChargeRateItem(ChargeRateItem item)
    {
        TransferChargeRateRepos repos = (TransferChargeRateRepos) getRepos();

        return repos.findByChargeRateItem(item);
    }
}
