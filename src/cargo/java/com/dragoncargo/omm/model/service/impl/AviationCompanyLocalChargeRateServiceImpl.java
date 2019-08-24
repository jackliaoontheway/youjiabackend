package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.AviationCompanyLocalChargeRate;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.repository.AviationCompanyLocalChargeRateRepos;
import com.dragoncargo.omm.model.service.AviationCompanyLocalChargeRateService;

@Service
public class AviationCompanyLocalChargeRateServiceImpl extends AbstractChargeRateServiceImpl<AviationCompanyLocalChargeRate>
        implements AviationCompanyLocalChargeRateService
{

    @Override
    public List<AviationCompanyLocalChargeRate> findByChargeRateItem(ChargeRateItem item)
    {
        AviationCompanyLocalChargeRateRepos repos = (AviationCompanyLocalChargeRateRepos) getRepos();

        return repos.findByChargeRateItem(item);
    }
}
