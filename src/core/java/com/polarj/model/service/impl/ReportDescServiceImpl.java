package com.polarj.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.ReportDesc;
import com.polarj.model.repository.ReportDescRepos;
import com.polarj.model.service.ReportDescService;

@Service
public class ReportDescServiceImpl extends EntityServiceImpl<ReportDesc, Integer> implements ReportDescService
{

    @Override
    public ReportDesc fetchEntityByCode(String code, String languageId)
    {
        ReportDescRepos repos = (ReportDescRepos) getRepos();
        ReportDesc r = repos.findFirstByCode(code);
        return replaceI18nFieldValueWithResource(dataClone(r), languageId);
    }

}
