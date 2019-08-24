package com.polarj.workflow.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.common.web.controller.ModelController;
import com.polarj.workflow.model.WorkflowSpec;
import com.polarj.workflow.model.service.WorkflowSpecService;

@RestController
@RequestMapping("/workflowspecs")
public class WorkflowSpecController extends ModelController<WorkflowSpec, Integer, WorkflowSpecService>
{

    public WorkflowSpecController()
    {
        super(WorkflowSpec.class);
    }
}