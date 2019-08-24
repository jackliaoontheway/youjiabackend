package com.polarj.model.service.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polarj.common.CommonConstant;
import com.polarj.common.utility.FieldValueUtil;
import com.polarj.model.I18nResource;
import com.polarj.model.service.I18nResourceService;

@Service
public class I18nResourceServiceImpl extends CachedEntityServiceImpl<I18nResource> implements I18nResourceService
{
    @Override
    protected void setUniqueField()
    {
        Field[] fields = FieldValueUtil.getAllFields(I18nResource.class, logger);
        Field[] uniqueFields = new Field[2];
        for (Field f : fields)
        {
            if (f.getName().equals("i18nKey"))
            {
                uniqueFields[0] = f;
            }
            if (f.getName().equals("languageId"))
            {
                uniqueFields[1] = f;
            }
        }
        if (uniqueFields[0] != null && uniqueFields[1] != null)
        {
            setUniqueFields(uniqueFields);
        }
    }

    @Override
    public I18nResource fetchI18nResource(String i18nKey, String languageId)
    {
        String key = i18nKey == null ? "" : (i18nKey + "-" + (languageId == null ? "" : languageId + "-"));
        return fetchFromCacheByUniqueValue(key, languageId);
    }

    @Override
    public List<I18nResource> fetchI18nResourceFor(String enumClassName)
    {
        return fetchFromCacheByUniqueValuePattern(enumClassName, CommonConstant.defaultSystemLanguage);
    }

    @Override
    public List<I18nResource> fetchI18nResourceBy(String i18nKey)
    {
        return fetchFromCacheByUniqueValuePattern(i18nKey + "-", CommonConstant.defaultSystemLanguage);
    }

}
