package com.polarj.workflow.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.polarj.workflow.model.WorkflowSpec;
import com.polarj.workflow.model.WorkflowStep;
import com.polarj.workflow.model.repository.WorkflowSpecRepos;
import com.polarj.workflow.model.service.WorkflowSpecService;

@Service
public class WorkflowSpecServiceImpl extends EntityServiceImpl<WorkflowSpec, Integer>
        implements WorkflowSpecService
{
    public WorkflowSpec fetchLatestWorkflow(String code)
    {
        WorkflowSpecRepos repos = (WorkflowSpecRepos)getRepos();
        List<WorkflowSpec> workflows = repos.findByCodeOrderByVersionDesc(code);
        if(workflows==null || workflows.isEmpty())
        {
            return null;
        }
        WorkflowSpec workflow = workflows.get(0);
        return dataClone(workflow);
    }
    
    public List<WorkflowStep> fetchWorkflowSteps(String code)
    {
        WorkflowSpec workflow = fetchLatestWorkflow(code);
        if(workflow!=null)
        {
            return workflow.getSteps();
        }
        return null;
    }
}
