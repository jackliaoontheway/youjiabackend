package com.polarj.workflow.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.workflow.model.CopperWait;

public interface CopperWaitRepos extends JpaRepository<CopperWait, String>
{
    public List<CopperWait> findByWorkflowInstanceId(String workflowInstanceId);
}
