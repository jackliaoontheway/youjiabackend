package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.ReportResult;
import com.polarj.model.service.ReportResultService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/reportresults")
public class ReportResultController
        extends ModelController<ReportResult, Integer, ReportResultService>
{
    public ReportResultController()
    {
        super(ReportResult.class);
    }
}
