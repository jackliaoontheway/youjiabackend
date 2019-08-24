package com.polarj.workflow.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.workflow.model.WorkflowSpec;

public interface WorkflowSpecRepos extends JpaRepository<WorkflowSpec, Integer>
{
    // 找出代码为code的所有版本的工作流，按照降序排序后得到最高版本，最新的版本
    public List<WorkflowSpec> findByCodeOrderByVersionDesc(String code);
}
