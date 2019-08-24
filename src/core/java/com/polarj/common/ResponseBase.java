package com.polarj.common;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public abstract class ResponseBase<T>
{
    private @Getter @Setter List<StatusInfo> statusList;

    private @Getter List<T> dataList;

    public T fetchOneData()
    {
        if (dataList != null && dataList.size() == 1)
        {
            return dataList.get(0);
        }
        return null;
    }

    public boolean hasData()
    {
        if (dataList == null || dataList.size() == 0)
        {
            return false;
        }
        return true;
    }

    public StatusInfo fetchFirstError()
    {
        if (statusList == null || statusList.size() == 0)
        {
            return null;
        }
        for (StatusInfo status : statusList)
        {
            if (status.isError())
            {
                return status;
            }
        }
        return null;
    }

    public boolean hasError()
    {
        if (statusList == null || statusList.size() == 0)
        {
            return false;
        }
        for (StatusInfo status : statusList)
        {
            if (status.isError())
            {
                return true;
            }
        }
        return false;
    }

    public void addData(T data)
    {
        if (dataList == null)
        {
            dataList = new ArrayList<T>();
        }
        if (data != null)
        {
            dataList.add(data);
        }
    }

    public void addDatas(List<T> datas)
    {
        if (dataList == null)
        {
            dataList = new ArrayList<T>();
        }
        if (datas != null && datas.size() > 0)
        {
            dataList.addAll(datas);
        }
    }

    public void addStatus(StatusInfo status)
    {
        if (statusList == null)
        {
            statusList = new ArrayList<StatusInfo>();
        }
        if (status != null)
        {
            statusList.add(status);
        }
    }

    public void addStatusList(List<StatusInfo> statusList)
    {
        if (this.statusList == null)
        {
            this.statusList = new ArrayList<StatusInfo>();
        }
        if (statusList != null && statusList.size() > 0)
        {
            this.statusList.addAll(statusList);
        }
    }

    public String toString()
    {
        String res = "No status.";
        if (statusList == null)
        {
            return res;
        }
        res += "[";
        for (int i = 0; i < statusList.size(); i++)
        {
            StatusInfo status = statusList.get(i);
            if (i == 0)
            {
                res += status.toString();
            }
            else
            {
                res += ", " + status.toString();
            }
        }
        res += "]";
        return res;
    }

}
