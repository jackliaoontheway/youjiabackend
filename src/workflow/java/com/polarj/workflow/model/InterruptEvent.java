package com.polarj.workflow.model;

import java.io.Serializable;
import java.util.Date;

import com.polarj.workflow.model.enumeration.InterruptEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用于給工作流發控制信息
 * 
 * @author Atomic
 *
 */
public @ToString class InterruptEvent<T> implements Serializable
{
    private static final long serialVersionUID = 6072216434174020892L;

    /**
     * 事件id
     */
    private @Getter @Setter Long id;

    /**
     * 对应事件的关联ID(correlationId), 应该跟跟等待事件的关联ID一致
     */
    private final @Getter String correlationId;

    /**
     * 回复结果的枚举, 应该是waitEventEnum所允许的结果
     */
    private final @Getter InterruptEnum interruptEnum;

    /**
     * 处理人的accountId
     */
    private final @Getter String processedBy;

    /**
     * 发送此回复的对象的关联ID,sender正在等待此ID, 可以为空 optional
     */
    private @Getter @Setter String senderCId;

    /**
     * 发送此回复的对象(sender)的关联Event的id,sender正在等待此事件的回复, 可以为空 optional
     */
    private @Getter @Setter Long senderEventId;

    /**
     * 处理的时间
     */
    private @Getter @Setter Date processedTime;

    /**
     * 额外的处理说明,可以为空
     */
    private @Getter @Setter String note;

    /**
     * Used when ResultEnum = ResultEnum.UPDATE
     */
    private @Getter @Setter T updateRequest;

    public InterruptEvent(String correlationId, InterruptEnum resultEnum, String processedBy)
    {
        super();
        this.correlationId = correlationId;
        this.interruptEnum = resultEnum;
        this.processedBy = processedBy;
    }
}
