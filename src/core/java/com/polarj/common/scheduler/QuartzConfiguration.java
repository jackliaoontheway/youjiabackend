package com.polarj.common.scheduler;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.polarj.common.CommonConstant;
import com.polarj.common.service.impl.PolarJQuartzService;
import com.polarj.model.service.SchedulerService;

@Configuration
@ConditionalOnProperty("scheduler.enable")
public class QuartzConfiguration
{
    private final Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    @Autowired
    @Lazy
    private AutowireCapableBeanFactory capableBeanFactory;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    @Lazy
    private PolarJQuartzService quartzService;

    @Bean
    @Primary
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException
    {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(propertiesFactoryBean.getObject());
        factory.setJobFactory(jobFactory());
        return factory;
    }

    private AdaptableJobFactory jobFactory()
    {
        return new AdaptableJobFactory()
        {
            @Override
            protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception
            {
                Object jobInstance = super.createJobInstance(bundle);
                capableBeanFactory.autowireBean(jobInstance);
                return jobInstance;
            }
        };
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() throws IOException
    {
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        if (schedulerService == null)
        {
            logger.info("Can not find the service for Entity Scheduler.");
            return scheduler;
        }
        List<com.polarj.model.Scheduler> schs = schedulerService.list(CommonConstant.defaultSystemLanguage);

        if (CollectionUtils.isEmpty(schs))
        {
            logger.info("No predefined task found.");
            return scheduler;
        }
        for (com.polarj.model.Scheduler sch : schs)
        {
            quartzService.addJob(scheduler, sch);
        }
        return scheduler;
    }
}
