package com.polarj.initial;

import org.apache.commons.lang3.StringUtils;

import com.polarj.model.CountryISO3166;
import com.polarj.model.service.CountryISO3166Service;

public class InitCountry extends InitializeDataFromJSON<CountryISO3166, CountryISO3166Service>
{
    InitCountry()
    {
        super(CountryISO3166.class, CountryISO3166[].class, CountryISO3166Service.class);
    }

    @Override
    protected String getJSONFileName()
    {
        return "CountryISO3166.json";
    }

    @Override
    protected String languageUsedInTheFile()
    {
        return "zh-cn";
    }

    @Override
    protected boolean isExisting(CountryISO3166 entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getCode()))
        {
            return false;
        }
        CountryISO3166 m = getService().fetchCountryByCode(entity.getCode(), languageUsedInTheFile());
        return m == null ? false : true;
    }
}
