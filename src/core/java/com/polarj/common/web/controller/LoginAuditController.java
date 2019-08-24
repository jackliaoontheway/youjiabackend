package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.LoginAudit;
import com.polarj.model.service.LoginAuditService;

@RestController
@RequestMapping("/loginaudits")
public class LoginAuditController extends ModelController<LoginAudit, Integer, LoginAuditService>
{

    public LoginAuditController()
    {
        super(LoginAudit.class);
    }
}