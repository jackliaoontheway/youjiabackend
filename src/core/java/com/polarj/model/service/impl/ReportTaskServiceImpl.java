package com.polarj.model.service.impl;

import com.polarj.model.service.impl.EntityServiceImpl;

import org.springframework.stereotype.Service;

import com.polarj.model.ReportTask;
import com.polarj.model.service.ReportTaskService;

@Service
public class ReportTaskServiceImpl extends EntityServiceImpl<ReportTask, Integer>
        implements ReportTaskService
{
}
