package com.polarj.model.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// 注释系统中的准备作为定时任务的类。
// 使用本注释描述作为持久化的初始化值
// 系统在启动时，直接使用持久化信息。
// 定时任务的缺省运行方式为： 系统启动后不延迟就执行指定的定时任务，每60秒运行一次
@Retention(RUNTIME)
@Target(TYPE)
public @interface SchedulerInfo
{
    String name();

    int maxStuckNum();

    int executorId() default 0;

    int startDelayTime() default 0;

    int repeatInterval() default 60;

    String cronExpression() default "";
}
