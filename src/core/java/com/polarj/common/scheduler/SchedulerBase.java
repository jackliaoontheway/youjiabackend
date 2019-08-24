package com.polarj.common.scheduler;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.polarj.common.CommonConstant;
import com.polarj.model.Scheduler;
import com.polarj.model.service.SchedulerService;

import lombok.Setter;

// 该类的子类一般都是用于周期性的任务
// 可以在该类的子类加SchedulerInfo的注解，用于描述持久化的初始化值
// 在初始化代码中把做好的该类的子类类名加入代码就可以使用注解的值进行初始化，并确保被系统运行
public abstract class SchedulerBase extends QuartzJobBean
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 该定时任务是否正在执行？
    private boolean running = false;

    private final String schedulerName = getClass().getSimpleName();

    @Autowired
    private SchedulerService schedulerService;

    protected static String languageId = CommonConstant.defaultSystemLanguage;

    protected static Integer sysOperId = CommonConstant.systemUserAccountId;

    private int stuckCount = 0;

    // 每个运行环境配置一个Executor Id，
    // 每个定时任务有Executor Id，
    // （暂时没有实现）每个定时任务的操作对象有Executor Id
    // 只有定时任务的Executor Id和运行环境的Executor Id一致，
    // 该定时任务才能在该运行环境下执行
    private @Setter int evnExecutorId;

    // 在没有做任何定时任务配置的情况下，
    // 用于测试定时任务的功能
    // ignoreConfiguration: 是否忽略数据库中的参数设置，
    // 选择忽略，只是测试定时任务的功能，后面的running和stuckCount参数不起作用
    // 选择不忽略，可以根据running和stuckCount的两个参数测试生产环境下的一些情况
    public void testScheduledTask(boolean ignoreConfiguration, boolean running, int stuckCount)
    {
        this.running = running;
        this.stuckCount = stuckCount;
        scheduledTask(ignoreConfiguration);
    }

    private void scheduledTask(boolean ignoreConfiguration)
    {
        if (!runnable(ignoreConfiguration))
        {
            logger.info("Task " + schedulerName + " is not runnable.");
            return;
        }
        try
        {
            logger.info("======= Begin to run " + schedulerName + ". =======");
            Long startTime = System.currentTimeMillis();
            task();
            Long endTime = System.currentTimeMillis();
            logger.info(schedulerName + " runs " + (endTime - startTime) / 1000 + "s.");
            running = false;
            logger.info("======= " + schedulerName + " End =======");
        }
        catch (Exception e)
        {
            // QUES： 一旦出错，是否应该把stuck count直接设置为最大值？
            logger.error(e.getMessage(), e);
        }
    }

    // 应该仅仅用于使用配置文件在运行环境的配置文件中使用
    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException
    {
        scheduledTask(false);
    }

    protected abstract void task();

    // force: 是否强制定时任务执行
    // 根据语言特性，保证该方法只会在线程的方式下同时只有一个线程使用。
    synchronized private boolean runnable(boolean ignoreConfiguration)
    {
        if (ignoreConfiguration)
        {
            return true;
        }
        Scheduler scheduler =
                schedulerService.fetch(schedulerName, evnExecutorId, CommonConstant.defaultSystemLanguage);
        if (scheduler == null)
        {
            // 系统配置数据库中找不到对应的定时任务
            logger.error("Can not get scheduled task by name=" + schedulerName);
            return false;
        }
        if (scheduler.getActive() != null && !scheduler.getActive())
        {
            // 找到的定时任务没有被激活
            logger.info("Task " + schedulerName + " has been disabled.");
            return false;
        }
        if (running)
        {
            // 该定时任务的前一次执行还没有结束，本次不执行，卡住数量增加
            logger.info("The last sheduled task is still running. Will wait for next time...");
            stuckCount++;
            scheduler.setStuckCount(stuckCount);
            if (stuckCount > scheduler.getMaxStuckNum())
            {
                // 超过设定的最大卡住数量的，关掉定时任务
                scheduler.setActive(false);
            }
            schedulerService.update(scheduler.getId(), scheduler, CommonConstant.systemUserAccountId,
                    CommonConstant.defaultSystemLanguage);
            return false;
        }
        // 定时任务可以执行
        stuckCount = 0;
        if (scheduler.getStuckCount() == null || scheduler.getStuckCount() != 0)
        {
            scheduler.setStuckCount(0);
        }
        scheduler.setRunTime(new Date());
        schedulerService.update(scheduler.getId(), scheduler, CommonConstant.systemUserAccountId,
                CommonConstant.defaultSystemLanguage);
        running = true;
        return true;
    }
}
