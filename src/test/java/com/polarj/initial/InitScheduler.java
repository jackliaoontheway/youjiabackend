package com.polarj.initial;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;

import com.polarj.common.utility.ReflectionUtil;
import com.polarj.model.Scheduler;
import com.polarj.model.annotation.SchedulerInfo;
import com.polarj.model.service.SchedulerService;

import lombok.Setter;

public class InitScheduler extends AbstractInitializeData<Scheduler, SchedulerService>
{
    private @Setter List<String> packageNames;

    InitScheduler()
    {
        super(Scheduler.class, SchedulerService.class);
    }

    @Override
    protected String initialData(boolean removeExistingData)
    {
        if (CollectionUtils.isEmpty(packageNames))
        {
            return "No scan-package found.";
        }
        return super.initialData(removeExistingData);
    }

    private Scheduler convertFrom(SchedulerInfo info, Class<?> clazz)
    {
        Scheduler s = new Scheduler();
        s.setCode(clazz.getSimpleName());
        s.setName(info.name());
        s.setImplementationClass(clazz.getName());
        s.setMaxStuckNum(info.maxStuckNum());
        s.setExecutorId(info.executorId());
        s.setActive(Boolean.FALSE);
        s.setCronExpression(info.cronExpression());
        s.setStartDelayTime(info.startDelayTime());
        s.setRepeatInterval(info.repeatInterval());
        return s;
    }

    @Override
    protected List<Scheduler> fetchDataFromDataSource()
    {
        List<Scheduler> schedulers = new ArrayList<>();
        for (String packageName : packageNames)
        {
            List<Class<?>> clazzes = ReflectionUtil.listClassWithAnnotationUnderPackage(packageName,
                    SchedulerInfo.class);
            if (clazzes == null || clazzes.size() == 0)
            {
                logger.info("No class in package {} with annotation SchedulerInfo.", packageName);
                continue;
            }
            for (Class<?> clazz : clazzes)
            {
                Scheduler s = convertFrom(clazz.getAnnotation(SchedulerInfo.class), clazz);
                if (s != null)
                {
                    schedulers.add(s);
                }
            }
        }
        return schedulers;
    }

    @Override
    protected String languageUsedInDataSource()
    {
        return languageId;
    }

    @Override
    protected boolean isExisting(Scheduler entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getCode()))
        {
            return false;
        }
        Scheduler m = getService().fetch(entity.getCode(), 0, languageUsedInDataSource());
        return m == null ? false : true;
    }
}
