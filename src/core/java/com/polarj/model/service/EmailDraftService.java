package com.polarj.model.service;

import java.util.List;

import com.polarj.model.EmailDraft;

public interface EmailDraftService extends EntityService<EmailDraft, Integer>
{
    List<EmailDraft> listNewDraftEmail(int limit);
}
