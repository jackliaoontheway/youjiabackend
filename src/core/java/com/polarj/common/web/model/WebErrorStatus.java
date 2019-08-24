package com.polarj.common.web.model;

import com.polarj.common.StatusInfo;

public class WebErrorStatus extends StatusInfo
{
    private final static String WEB_PREFIX = "SYSTEM.ERROR.";

    public final static WebErrorStatus noUserError = new WebErrorStatus("web.auth", "Incorrect user name or password!");

    public final static WebErrorStatus noLoginUserError =
            new WebErrorStatus("web.auth", "No login user for now, Please login again!");

    public final static WebErrorStatus noRightError =
            new WebErrorStatus("web.auth", "You can not access this.");

    public WebErrorStatus(String code, String desc)
    {
        super(WEB_PREFIX + code, StatusLevel.ERROR, desc);
    }

    public WebErrorStatus(String desc)
    {
        super(WEB_PREFIX + "Exception", StatusLevel.ERROR, desc);
    }
}
