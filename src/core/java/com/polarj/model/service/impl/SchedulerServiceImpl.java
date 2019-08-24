package com.polarj.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.common.service.impl.PolarJQuartzService;
import com.polarj.model.Scheduler;
import com.polarj.model.repository.SchedulerRepos;
import com.polarj.model.service.SchedulerService;

@Service
public class SchedulerServiceImpl extends EntityServiceImpl<Scheduler, Integer> implements SchedulerService
{
    @Autowired
    private PolarJQuartzService quartzService;

    @Override
    public Scheduler fetch(String code, int executorId, String languageId)
    {
        SchedulerRepos repos = (SchedulerRepos) getRepos();
        Scheduler scheduler = repos.findFirstByCodeAndExecutorId(code, executorId);
        return replaceI18nFieldValueWithResource(scheduler, languageId);
    }

    @Override
    protected void doSomethingBeforeReturn(Scheduler sch)
    {
        sch.setQuartzStatus(quartzService.getJobStatus(sch));
    }
}
