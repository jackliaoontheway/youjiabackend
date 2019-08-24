package com.polarj.model.service;

import org.springframework.transaction.annotation.Transactional;

import com.polarj.model.ReportDesc;

public interface ReportDescService extends EntityService<ReportDesc, Integer>
{
    @Transactional
    public ReportDesc fetchEntityByCode(String code, String languageId);
}
