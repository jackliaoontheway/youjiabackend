package com.polarj.model.enumeration;

public enum SupportedLanguage implements I18nResourceKey
{
    ZH("zh-cn"), EN("en-us");

    private String value;

    private SupportedLanguage(String value)
    {
        this.value = value;
    }

    @Override
    public String getI18nResourceKey()
    {
        return value;
    }
}
