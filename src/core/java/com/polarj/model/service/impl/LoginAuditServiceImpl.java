package com.polarj.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.LoginAudit;
import com.polarj.model.repository.LoginAuditRepos;
import com.polarj.model.service.LoginAuditService;

@Service
public class LoginAuditServiceImpl extends EntityServiceImpl<LoginAudit, Integer> implements LoginAuditService
{
    public LoginAudit fetchByJseesionId(String jsessionId)
    {
        LoginAuditRepos repos = (LoginAuditRepos) getRepos();
        LoginAudit res = repos.findFirstByJsessionIdOrderByLoginTimeDesc(jsessionId);
        return res;
    }

}
