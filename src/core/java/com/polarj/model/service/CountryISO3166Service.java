package com.polarj.model.service;

import com.polarj.model.service.EntityService;
import com.polarj.model.CountryISO3166;

public interface CountryISO3166Service extends EntityService<CountryISO3166, Integer>
{
    CountryISO3166 fetchCountryByCode(String code, String languageId);
}
