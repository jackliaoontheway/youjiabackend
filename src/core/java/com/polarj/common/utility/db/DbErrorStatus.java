package com.polarj.common.utility.db;

import com.polarj.common.StatusInfo;

public class DbErrorStatus extends StatusInfo
{
    private final static String DB_PREFIX = "DB.ERROR.";

    public final static DbErrorStatus INSERT_ERROR = new DbErrorStatus("1001", "DB insert fail");

    public final static DbErrorStatus UPDATE_ERROR = new DbErrorStatus("1002", "DB update fail");

    public final static DbErrorStatus DELETE_ERROR = new DbErrorStatus("1003", "DB delete fail");

    public final static DbErrorStatus FIND_ERROR = new DbErrorStatus("1004", "DB find fail");

    public final static DbErrorStatus PARAM_ERROR = new DbErrorStatus("1005", "DB parameter error");

    public final static DbErrorStatus COUNT_ERROR = new DbErrorStatus("1006", "DB count error");

    public DbErrorStatus(String code, String desc)
    {
        super(DB_PREFIX + code, StatusLevel.ERROR, desc);
    }
}
