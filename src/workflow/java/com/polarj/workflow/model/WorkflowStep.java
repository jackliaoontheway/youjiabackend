package com.polarj.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="workflowstep")
public class WorkflowStep extends GenericDbInfo
{
    private static final long serialVersionUID = 6049833242094034800L;

    // 步骤代码/名称， 在同一个工作流里面要唯一
    @FieldMetaData(position=10, label="Code", autogenerated=true)
    @Column(length = 64)
    private @Getter @Setter String code;

    // 步骤描述，需要国际化
    @FieldMetaData(position=20, label="Description", maxLength=1000, hide=true)
    @Column(length = 1024)
    private @Getter @Setter String description;

    // 进入此状态的前置事件，如果为空就是第一个步骤， 
    // 一个工作流中只能有一个triggerEvents为空的步骤
    // 是由上一个步骤+"-"+上一个步骤操作的返回值共同构成
    // 可以有多个，用","分开
    @FieldMetaData(position=30, label="Trigger Events", autogenerated=true)
    @Column(length = 1024)
    private @Getter @Setter String triggerEvents;

    // 本步骤需要执行的操作
    @FieldMetaData(position=40, label="Action Class", autogenerated=true)
    @Column(length = 512)
    private @Getter @Setter String actionClass;

    // 本步骤下的操作可能返回值
    // 多个返回值使用","分开
    @FieldMetaData(position=50, label="Result Events", autogenerated=true)
    @Column(length = 1024)
    private @Getter @Setter String resultEvents;

    // 此步骤下最长的等待时间
    @FieldMetaData(position=60, dataType="number", label="Wait Seconds", autogenerated=true)
    @Column
    private @Getter @Setter Integer waitSecond;

    // 超时之后需要执行的操作
    @FieldMetaData(position=70, label="Action Class for Timeout", autogenerated=true)
    @Column(length = 512)
    private @Getter @Setter String timeoutActionClass;
}
