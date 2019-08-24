package com.polarj.api;

import com.polarj.common.StatusInfo;

public abstract class APIResponseStatus extends StatusInfo
{
    public APIResponseStatus(String code, StatusLevel level, String desc)
    {
        super("API." + code, level, desc);
    }
}
