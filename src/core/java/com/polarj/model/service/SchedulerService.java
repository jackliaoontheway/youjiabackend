package com.polarj.model.service;

import org.springframework.transaction.annotation.Transactional;

import com.polarj.model.Scheduler;

public interface SchedulerService extends EntityService<Scheduler, Integer>
{
    @Transactional
    public Scheduler fetch(String code, int executorId, String languageId);
}
