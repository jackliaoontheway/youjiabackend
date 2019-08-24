package com.polarj.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.CurrencyISO4217;
import com.polarj.model.repository.CurrencyISO4217Repos;
import com.polarj.model.service.CurrencyISO4217Service;

@Service
public class CurrencyISO4217ServiceImpl extends EntityServiceImpl<CurrencyISO4217, Integer>
        implements CurrencyISO4217Service
{
    public CurrencyISO4217 fetchByCode(String code, String languageId)
    {
        CurrencyISO4217Repos repos = (CurrencyISO4217Repos) this.getRepos();
        CurrencyISO4217 entity = repos.findFirstByCode(code);
        return replaceI18nFieldValueWithResource(dataClone(entity), languageId);
    }
}
