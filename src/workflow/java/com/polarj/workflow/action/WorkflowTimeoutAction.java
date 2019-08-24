package com.polarj.workflow.action;

import com.polarj.workflow.TimeoutAction;
import com.polarj.workflow.model.ModelWithWorkflowStep;

public interface WorkflowTimeoutAction
{
    public TimeoutAction execute(ModelWithWorkflowStep model, String step);
}
