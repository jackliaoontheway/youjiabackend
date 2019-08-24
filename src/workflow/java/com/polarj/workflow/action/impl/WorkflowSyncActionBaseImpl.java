package com.polarj.workflow.action.impl;

import java.util.List;

import com.polarj.workflow.model.ActionResponse;
import com.polarj.workflow.model.ModelWithWorkflowStep;

// 如果一个动作是可以同步完成的，就从该类继承。
public abstract class WorkflowSyncActionBaseImpl extends WorkflowActionBaseImpl
{
    @Override
    final public ActionResponse execute(ModelWithWorkflowStep model, String cid, List<String> expectedResults)
    {
        return executeSync(model, cid, expectedResults);
    }
}
