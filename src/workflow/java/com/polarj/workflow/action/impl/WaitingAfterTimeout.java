package com.polarj.workflow.action.impl;

import com.polarj.workflow.TimeoutAction;
import com.polarj.workflow.action.WorkflowTimeoutAction;
import com.polarj.workflow.model.ModelWithWorkflowStep;

public class WaitingAfterTimeout implements WorkflowTimeoutAction
{

    @Override
    public TimeoutAction execute(ModelWithWorkflowStep model, String step)
    {
        return TimeoutAction.WAITING;
    }

}
