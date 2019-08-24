package com.polarj.workflow.model.service;

import java.util.List;

import com.polarj.model.service.EntityService;
import com.polarj.workflow.model.WorkflowSpec;
import com.polarj.workflow.model.WorkflowStep;

public interface WorkflowSpecService extends EntityService<WorkflowSpec, Integer>
{
    public WorkflowSpec fetchLatestWorkflow(String code);
    
    // 找到code工作流最后版本的所有的工作流步骤
    public List<WorkflowStep> fetchWorkflowSteps(String code);
}
