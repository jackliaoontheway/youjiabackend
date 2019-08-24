package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.ChargeRateItemCategory;
import com.dragoncargo.omm.model.repository.ChargeRateItemCategoryRepos;
import com.dragoncargo.omm.model.service.ChargeRateItemCategoryService;
import com.polarj.model.service.impl.EntityServiceImpl;

@Service
public class ChargeRateItemCategoryServiceImpl extends EntityServiceImpl<ChargeRateItemCategory, Integer>
        implements ChargeRateItemCategoryService
{

    @Override
    public ChargeRateItemCategory findByCode(String chargeRateCategoryCode)
    {
        ChargeRateItemCategoryRepos repos = (ChargeRateItemCategoryRepos) getRepos();

        return repos.findFirstByCode(chargeRateCategoryCode);
    }

    @Override
    public List<ChargeRateItemCategory> findCategoryForMultipleQuery()
    {
        ChargeRateItemCategoryRepos repos = (ChargeRateItemCategoryRepos) getRepos();

        return repos.findByMultipleQueryAvailable(true);
    }
}
