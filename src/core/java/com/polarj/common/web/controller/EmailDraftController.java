package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.EmailDraft;
import com.polarj.model.service.EmailDraftService;

@RestController
@RequestMapping("/emaildrafts")
public class EmailDraftController extends ModelController<EmailDraft, Integer, EmailDraftService>
{
    public EmailDraftController()
    {
        super(EmailDraft.class);
    }
}