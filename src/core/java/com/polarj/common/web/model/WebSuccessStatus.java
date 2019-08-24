package com.polarj.common.web.model;

import com.polarj.common.StatusInfo;

public class WebSuccessStatus extends StatusInfo
{
    private final static String WEB_PREFIX = "SYSTEM.INFO.";

    public WebSuccessStatus(String code, String desc)
    {
        super(WEB_PREFIX + code, StatusLevel.INFO, desc);
    }

    public WebSuccessStatus(String desc)
    {
        super(WEB_PREFIX, StatusLevel.INFO, desc);
    }
}
