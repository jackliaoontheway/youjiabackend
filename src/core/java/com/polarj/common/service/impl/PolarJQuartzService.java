package com.polarj.common.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolarJQuartzService
{
    private Logger logger = LoggerFactory.getLogger(PolarJQuartzService.class);
    private final String JobGroupName = "PolarJGroup";
    @Autowired
    private Scheduler quartzScheduler;

    public boolean resumeJob(com.polarj.model.Scheduler sch)
    {
        if (!isJobExist(sch))
        {
            logger.info("{}:{} is not exist, nothing will be resumed.", sch.getCode(), sch.getName());
            return false;
        }
        boolean result = true;

        JobKey jobKey = JobKey.jobKey(sch.getCode(), JobGroupName);
        try
        {
            boolean hasPausedTrigger = false;
            List<? extends Trigger> triggers = quartzScheduler.getTriggersOfJob(jobKey);
            if (CollectionUtils.isEmpty(triggers))
            {
                for (Trigger trigger : triggers)
                {
                    if (quartzScheduler.getTriggerState(trigger.getKey()).equals(TriggerState.PAUSED))
                    {
                        hasPausedTrigger = true;
                        break;
                    }
                }
            }
            if (hasPausedTrigger)
            {
                quartzScheduler.resumeJob(jobKey);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

    public boolean pauseJob(com.polarj.model.Scheduler sch)
    {
        if (!isJobExist(sch))
        {
            logger.info("{}:{} is not exist, nothing will be paused.", sch.getCode(), sch.getName());
            return false;
        }
        boolean result = true;

        JobKey jobKey = JobKey.jobKey(sch.getCode(), JobGroupName);
        try
        {
            quartzScheduler.pauseJob(jobKey);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

    public boolean removeJob(com.polarj.model.Scheduler sch)
    {
        if (!isJobExist(sch))
        {
            logger.info("{}:{} is not exist, nothing will be removed.", sch.getCode(), sch.getName());
            return false;
        }
        boolean result = true;
        JobKey jobKey = JobKey.jobKey(sch.getCode(), JobGroupName);
        try
        {
            quartzScheduler.pauseJob(jobKey);
            List<? extends Trigger> triggers = quartzScheduler.getTriggersOfJob(jobKey);
            if (CollectionUtils.isEmpty(triggers))
            {
                for (Trigger trigger : triggers)
                {
                    quartzScheduler.unscheduleJob(trigger.getKey());
                }
            }
            quartzScheduler.deleteJob(jobKey);

        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

    public boolean rescheduleJob(com.polarj.model.Scheduler sch)
    {
        boolean result = true;
        if (!isJobExist(sch))
        {
            logger.info("{}:{} is not exist, nothing will be updated.", sch.getCode(), sch.getName());
            return false;
        }
        Trigger newTrigger = generateTrigger(sch);
        TriggerKey triggerKey = TriggerKey.triggerKey(sch.getCode(), JobGroupName);
        try
        {
            quartzScheduler.rescheduleJob(triggerKey, newTrigger);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

    public boolean addJob(Scheduler quartzScheduler, com.polarj.model.Scheduler sch)
    {
        if (sch == null)
        {
            return false;
        }
        if (isJobExist(sch))
        {
            logger.info("{}:{} is exist.", sch.getCode(), sch.getName());
            return false;
        }
        if (this.quartzScheduler == null)
        {
            this.quartzScheduler = quartzScheduler;
        }
        if (this.quartzScheduler == null && quartzScheduler == null)
        {
            logger.error("No quartz scheduler exist.");
            return false;
        }
        Class<? extends Job> jobClass = getClass(sch.getImplementationClass());
        if (jobClass == null)
        {
            return false;
        }
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(sch.getCode(), JobGroupName).build();
        Trigger trigger = generateTrigger(sch);
        boolean result = true;
        if (trigger == null)
        {
            logger.error("Can not build trigger for {}: {}.", sch.getCode(), sch.getName());
            return false;
        }
        try
        {
            quartzScheduler.scheduleJob(jobDetail, trigger);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

    public String getJobStatus(com.polarj.model.Scheduler sch)
    {
        TriggerKey triggerKey = TriggerKey.triggerKey(sch.getCode(), JobGroupName);
        TriggerState triggerState = null;
        try
        {
            triggerState = quartzScheduler.getTriggerState(triggerKey);
            List<JobExecutionContext> runningJobs = quartzScheduler.getCurrentlyExecutingJobs();
            if (CollectionUtils.isNotEmpty(runningJobs))
            {
                for (JobExecutionContext runningJob : runningJobs)
                {
                    if (runningJob.getJobDetail().getKey().getName().equals(sch.getCode()))
                    {
                        return triggerState.name() + ": Running.";
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            triggerState = TriggerState.NONE;
        }
        return triggerState.name();
    }

    private Trigger generateTrigger(com.polarj.model.Scheduler sch)
    {
        ScheduleBuilder<? extends Trigger> scheduleBuilder = null;
        if (sch.getRepeatInterval() != null && sch.getRepeatInterval() > 0)
        {
            scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(sch.getRepeatInterval())
                    .repeatForever();
        }
        else if (StringUtils.isNotEmpty(sch.getCronExpression()))
        {
            scheduleBuilder = CronScheduleBuilder.cronSchedule(sch.getCronExpression());
        }
        if (scheduleBuilder == null)
        {
            logger.error("Can not create schedule for {}: {}.", sch.getCode(), sch.getName());
            return null;
        }
        Trigger trigger = null;
        // 从这个逻辑结构来说，我们是优先简单方式的定时任务，然后是Cron方式的任务
        // 因为这两个数据是在业务模型中新加的，所以要做保护性编程
        if (sch.getStartDelayTime() != null && sch.getStartDelayTime() > 0)
        {
            Date startDate = DateBuilder.futureDate(sch.getStartDelayTime(), IntervalUnit.SECOND);
            trigger = TriggerBuilder.newTrigger().withIdentity(sch.getCode(), JobGroupName)
                    .withSchedule(scheduleBuilder).startAt(startDate).build();
        }
        else if (StringUtils.isNotEmpty(sch.getCronExpression()))
        {
            try
            {
                trigger = TriggerBuilder.newTrigger().withIdentity(sch.getCode(), JobGroupName)
                        .withSchedule(scheduleBuilder).build();
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
                trigger = null;
            }
        }
        else
        {
            trigger = null;
        }
        return trigger;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Job> getClass(String jobClassName)
    {
        Class<? extends Job> jobClass = null;
        try
        {
            jobClass = (Class<? extends Job>) Class.forName(jobClassName);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return jobClass;
    }

    private boolean isJobExist(com.polarj.model.Scheduler sch)
    {
        if (sch == null)
        {
            return false;
        }
        JobDetail jobDetail = null;
        try
        {
            jobDetail = quartzScheduler.getJobDetail(JobKey.jobKey(sch.getCode()));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            jobDetail = null;
        }
        return ObjectUtils.allNotNull(jobDetail) ? true : false;
    }
}
