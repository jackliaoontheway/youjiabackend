package com.polarj.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polarj.model.EmailDraft;
import com.polarj.model.enumeration.EmailStatus;
import com.polarj.model.repository.EmailDraftRepos;
import com.polarj.model.service.EmailDraftService;

@Service
public class EmailDraftServiceImpl extends EntityServiceImpl<EmailDraft, Integer> implements EmailDraftService
{

    @Override
    public List<EmailDraft> listNewDraftEmail(int limit)
    {
        EmailDraftRepos repos = (EmailDraftRepos)getRepos();
        List<EmailDraft> records = repos.findFirst10ByStatus(EmailStatus.NEW.name());
        return records;
    }
    
}
