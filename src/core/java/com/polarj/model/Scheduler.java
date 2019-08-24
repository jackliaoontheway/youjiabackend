package com.polarj.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(showDetailFieldName = "code")
@Entity
@Table(name = "scheduler", indexes = { @Index(columnList = "code", name = "UK_Scheduler_code", unique = true) })
public @ToString @EqualsAndHashCode(callSuper = false) class Scheduler extends GenericDbInfo
{
    private static final long serialVersionUID = -2263493442797723272L;

    // 定时任务的代码，全局唯一，直接使用定时任务类的简单类名
    @FieldMetaData(position = 10, label = "Code", required = true, maxLength = 64)
    @I18nKeyField
    @Column(length = 128, nullable = false)
    private @Getter @Setter String code;

    // 定时任务的名称，使用定时任务类的简单名称
    @FieldMetaData(position = 20, label = "Name", maxLength = 120)
    @I18nField
    @Column(length = 128)
    private @Getter @Setter String name;

    // 定时任务的实现类全名，用于调度使用
    @FieldMetaData(position = 30, label = "Schedule Class", maxLength = 250)
    @Column(length = 255, nullable = false)
    private @Getter @Setter String implementationClass;

    // 定时任务的描述，应该是可以国际化的字段
    @FieldMetaData(position = 40, label = "Description", maxLength = 1000)
    @I18nField
    @Column(length = 1023)
    private @Getter @Setter String description;

    // 该任务是否为需要运行任务的标志， 缺省：不启动运行
    @FieldMetaData(position = 50, label = "Active", maxLength = 1000, dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column(nullable = false)
    private @Getter @Setter Boolean active;

    // 该定时任务没有运行的次数，次数超过maxStuckNum次，原则上认为由于
    // 某种原因，该任务出现了异常情况
    @FieldMetaData(position = 60, label = "Stuck Count", dataType = FieldMetaDataSupportedDataType.NUMBER, autogenerated = true)
    @Column
    private @Getter @Setter Integer stuckCount;

    // 允许该定时任务不执行的最高次数， 超过就需要人工干预
    @FieldMetaData(position = 70, label = "Max Stuck Number", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column(nullable = false)
    private @Getter @Setter Integer maxStuckNum;

    // 该定时任务在系统启动后延迟启动的时间长度（秒）
    @FieldMetaData(position = 71, label = "Start Delay Time", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Getter @Setter Integer startDelayTime;

    // 运行的间隔时间（秒）
    @FieldMetaData(position = 72, label = "Repeat Interval", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Getter @Setter Integer repeatInterval;

    // 提前定义好的基于CRON的运行规则
    @FieldMetaData(position = 79, label = "Cron Expression")
    @Column(length = 64)
    private @Getter @Setter String cronExpression;

    // 正在执行任务的开始时间
    @FieldMetaData(position = 80, label = "Begin Run Time", dataType = FieldMetaDataSupportedDataType.DATE, formatter = FieldMetaDataSupportedDataType.TIMEFORMAT, autogenerated = true)
    @Column
    private @Getter @Setter Date runTime;

    // 定时任务将要执行的运行环境编号， 缺省值为0
    @FieldMetaData(position = 90, label = "Run On", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column(nullable = false)
    private @Getter @Setter Integer executorId;

    @FieldMetaData(position = 2, label = "Status In Quartz")
    @Transient
    private @Getter @Setter String quartzStatus;
}
