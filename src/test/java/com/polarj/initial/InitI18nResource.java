package com.polarj.initial;

import org.apache.commons.lang3.StringUtils;

import com.polarj.model.I18nResource;
import com.polarj.model.service.I18nResourceService;


public class InitI18nResource extends InitializeDataFromCSV<I18nResource, I18nResourceService>
{

    public InitI18nResource()
    {
        super(I18nResource.class, I18nResourceService.class);
    }

    @Override
    protected String getCsvFileName()
    {
        return "i18nresource.csv";
    }

    @Override
    protected int getCsvFileColumnAmount()
    {
        return 3;
    }

    @Override
    protected I18nResource convertFromCsvRow(String[] row)
    {
        I18nResource res = new I18nResource();
        res.setI18nKey(row[0]);
        res.setLanguageId(row[1]);
        res.setI18nValue(row[2]);
        return res;
    }

    @Override
    protected String languageUsedInTheFile()
    {
        return "zh-cn";
    }

    @Override
    protected boolean isExisting(I18nResource entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getI18nKey()) || StringUtils.isEmpty(entity.getLanguageId()))
        {
            return false;
        }
        I18nResource m = getService().fetchI18nResource(entity.getI18nKey(), entity.getLanguageId());
        return m == null ? false : true;
    }
}
