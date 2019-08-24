package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.ReportTask;
import com.polarj.model.service.ReportTaskService;

@RestController
@RequestMapping("/reporttasks")
public class ReportTaskController extends UserRestrictionModelController<ReportTask, Integer, ReportTaskService>
{
    public ReportTaskController()
    {
        super(ReportTask.class);
    }
}
