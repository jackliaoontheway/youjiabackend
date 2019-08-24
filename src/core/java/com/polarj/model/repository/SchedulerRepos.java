package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.Scheduler;

public interface SchedulerRepos extends JpaRepository<Scheduler, Integer>
{
    public Scheduler findFirstByCodeAndExecutorId(String code, int executorId);
}
