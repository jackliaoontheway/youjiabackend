package com.polarj.model.service.impl;

import com.polarj.model.service.impl.EntityServiceImpl;

import org.springframework.stereotype.Service;

import com.polarj.model.ReportResult;
import com.polarj.model.service.ReportResultService;

@Service
public class ReportResultServiceImpl extends EntityServiceImpl<ReportResult, Integer>
        implements ReportResultService
{
}
