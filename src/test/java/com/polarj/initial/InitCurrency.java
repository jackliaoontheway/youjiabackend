package com.polarj.initial;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.CountryISO3166;
import com.polarj.model.CurrencyISO4217;
import com.polarj.model.service.CountryISO3166Service;
import com.polarj.model.service.CurrencyISO4217Service;

class InitCurrency extends InitializeDataFromJSON<CurrencyISO4217, CurrencyISO4217Service>
{
    private String supportedCountryInfoFileName = "supportedcountry.csv";

    InitCurrency()
    {
        super(CurrencyISO4217.class, CurrencyISO4217[].class, CurrencyISO4217Service.class);
    }

    @Override
    protected String getJSONFileName()
    {
        return "CurrencyISO4217.json";
    }

    @Override
    protected String languageUsedInTheFile()
    {
        return "zh-cn";
    }

    @Override
    protected String doSomethingAfterSavingEntities()
    {
        List<String[]> supportedCountries = readContentFromCsvFile(supportedCountryInfoFileName);
        if (CollectionUtils.isEmpty(supportedCountries))
        {
            String errMsg = "Can not find data in " + supportedCountryInfoFileName;
            return errMsg;
        }
        String result = null;
        try
        {
            CountryISO3166Service countryService = (CountryISO3166Service) SpringContextUtils
                    .getBean(CountryISO3166Service.class);
            if(countryService==null)
            {
                return "Can not find CountryISO3166Service.";
            }
            for(String[] supportedCountry: supportedCountries)
            {
                if(supportedCountry.length!=2)
                {
                    logger.error("Should have Country code and currency code inside supportedCountryInfoFileName. Please check the file.");
                    continue;
                }
                CountryISO3166 country = countryService.fetchCountryByCode(supportedCountry[0], languageId);
                if(country==null)
                {
                    logger.error("Can not find a country with code: {}. Please check the data.", supportedCountry[0]);
                    continue;
                }
                CurrencyISO4217 currency = getService().fetchByCode(supportedCountry[1], languageId);
                if(currency==null)
                {
                    logger.error("Can not find a currency with code: {}. Please check the data.", supportedCountry[1]);
                    continue;
                }
                country.setCurrency(currency);
                country = countryService.update(country.getId(), country, operId, languageId);
                if(country==null)
                {
                    logger.error("Can not save the updated country: {}, please check the data.", supportedCountry[0]);
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            result = null;
        }
        return result;
    }

    @Override
    protected boolean isExisting(CurrencyISO4217 entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getCode()))
        {
            return false;
        }
        CurrencyISO4217 m = getService().fetchByCode(entity.getCode(), languageUsedInTheFile());
        return m == null ? false : true;
    }
}
