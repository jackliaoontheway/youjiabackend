package com.polarj.model.service;

import com.polarj.model.LoginAudit;

public interface LoginAuditService extends EntityService<LoginAudit, Integer>
{
    LoginAudit fetchByJseesionId(String jsessionId);
}
