package com.polarj.common;

import lombok.Getter;

public abstract class StatusInfo
{
    // 使用 "Reference No: "+ timestamp 作为参考号，方便在日志文件中定位信息
    private String referenceNo;

    private StatusLevel level;

    private @Getter String code;

    private @Getter String desc;

    // 接口调用的状态信息分成三类
    protected static enum StatusLevel
    {
        INFO, WARN, ERROR
    };

    protected StatusInfo(String code, StatusLevel level, String desc)
    {
        this.code = code;
        this.desc = desc;
        this.level = level;
        this.referenceNo = "";
        if (level == StatusLevel.ERROR)
        {
            this.referenceNo = "Reference No: " + System.currentTimeMillis();
        }
    }

    public boolean isError()
    {
        return level == StatusLevel.ERROR ? true : false;
    }

    public String toString()
    {
        String s = referenceNo + " [" + level.name() + " - " + code + ": " + desc + "]";
        return s;
    }
}
