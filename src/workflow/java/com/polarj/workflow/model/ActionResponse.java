package com.polarj.workflow.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.polarj.common.ResponseBase;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用于回复的时候返回一个事件的处理结果
 * 
 * @author Atomic
 *
 */
public @ToString class ActionResponse extends ResponseBase<Object> implements Serializable
{

    private static final long serialVersionUID = 1433005726852420091L;

    /**
     * 事件id
     */
    private @Getter @Setter String id;

    /**
     * 对应事件的关联ID(correlationId), 应该跟跟等待事件的关联ID一致
     */
    private final @Getter String correlationId;

    /**
     * 是否需要等待,如果需要等待則工作流會等待事件處理完以後的notify, 如果不需要等待, 返回的结果有需要的内容， 默認為true
     */
    private @Getter @Setter boolean async = true;

    private @Getter @Setter boolean manullyNotification = false;
    
    private @Getter @Setter boolean redoCurAction = false;
    /**
     * 將整個workflow設成能取消與否
     */
    private @Getter @Setter Boolean flowCancellable;

    // 希望得到的结果，对于等待人工操作有用，只有在期望结果内的操作才进入等待工作流处理列表
    private @Getter @Setter List<String> expectedResults;
    /**
     * 发送此回复的对象的关联ID,sender正在等待此ID, 可以为空 optional
     */
    private @Getter @Setter String senderCId;

    /**
     * 发送此回复的对象(sender)的关联Event的id,sender正在等待此事件的回复, 可以为空 optional
     */
    private @Getter @Setter Long senderEventId;

    /**
     * 回复结果的枚举, 应该是waitEventEnum所允许的结果
     */
    private @Getter @Setter String resultEnum;

    /**
     * 处理的时间
     */
    private @Getter @Setter Date processedTime;

    /**
     * 处理人的accountId
     */
    private @Getter @Setter Integer processedBy;

    // 当前的页面语言是什么
    private @Getter @Setter String languageId;
    /**
     * 额外的处理说明,可以为空
     */
    private @Getter @Setter String note;

    /**
     * 是否跳過 - 目前沒有實現
     */
    private @Getter @Setter boolean rollforward;

    public ActionResponse(String correlationId)
    {
        super();
        this.correlationId = correlationId;
    }
}
