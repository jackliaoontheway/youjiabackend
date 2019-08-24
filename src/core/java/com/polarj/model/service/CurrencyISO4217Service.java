package com.polarj.model.service;

import com.polarj.model.service.EntityService;
import com.polarj.model.CurrencyISO4217;

public interface CurrencyISO4217Service extends EntityService<CurrencyISO4217, Integer>
{
    public CurrencyISO4217 fetchByCode(String code, String languageId);
}
