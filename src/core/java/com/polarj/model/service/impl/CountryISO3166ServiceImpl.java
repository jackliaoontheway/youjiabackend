package com.polarj.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.CountryISO3166;
import com.polarj.model.repository.CountryISO3166Repos;
import com.polarj.model.service.CountryISO3166Service;

@Service
public class CountryISO3166ServiceImpl extends EntityServiceImpl<CountryISO3166, Integer>
        implements CountryISO3166Service
{
    @Override
    public CountryISO3166 fetchCountryByCode(String code, String languageId)
    {
        CountryISO3166Repos repos = (CountryISO3166Repos) getRepos();
        CountryISO3166 res = repos.findFirstByCode(code);
        return replaceI18nFieldValueWithResource(dataClone(res), languageId);
    }

}
