package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.LoginAudit;

public interface LoginAuditRepos extends JpaRepository<LoginAudit, Integer>
{
    LoginAudit findFirstByJsessionIdOrderByLoginTimeDesc(String jsessionId);
}
