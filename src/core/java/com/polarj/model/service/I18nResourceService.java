package com.polarj.model.service;

import java.util.List;

import com.polarj.model.I18nResource;

public interface I18nResourceService extends EntityService<I18nResource, Integer>
{
    public I18nResource fetchI18nResource(String i18nKey, String languageId);

    public List<I18nResource> fetchI18nResourceFor(String enumClassName);

    public List<I18nResource> fetchI18nResourceBy(String i18nKey);
}
