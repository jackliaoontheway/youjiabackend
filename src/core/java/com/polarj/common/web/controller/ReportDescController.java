package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.ReportDesc;
import com.polarj.model.service.ReportDescService;

@RestController
@RequestMapping("/reports")
public class ReportDescController extends ModelController<ReportDesc, Integer, ReportDescService>
{

    public ReportDescController()
    {
        super(ReportDesc.class);
    }
}