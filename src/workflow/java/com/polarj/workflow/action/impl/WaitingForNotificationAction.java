package com.polarj.workflow.action.impl;

import com.polarj.workflow.model.ActionResponse;
import com.polarj.workflow.model.ModelWithWorkflowStep;

public class WaitingForNotificationAction extends WorkflowActionBaseImpl
{

    @Override
    protected void execute(ModelWithWorkflowStep model, ActionResponse res) throws Exception
    {
        res.setManullyNotification(true);
    }

}
