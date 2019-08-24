package com.polarj.workflow.model;

import com.polarj.common.StatusInfo;

public class WorkflowError extends StatusInfo
{
    public WorkflowError(String code, String desc)
    {
        super("wf." + code, StatusLevel.ERROR, desc);
    }
}
