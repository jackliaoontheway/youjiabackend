package com.polarj.workflow.action;

import java.util.List;

import com.polarj.workflow.model.ActionResponse;
import com.polarj.workflow.model.ModelWithWorkflowStep;

// 状态驱动的工作流中：
// 1. 每一个状态对应一个动作（Action）
// 2. 这个动作的执行要么立刻返回结果（同步方式）
// 3. 要么不返回结果，在执行完之后再通知工作流引擎结果（异步方式）
// ActionResponse中的async描述这个Action是同步还是异步方式执行，
// 工作流引擎需要根据这个标志进行相应的处理
// 实际使用中，都直接从WorkflowActionBaseImpl继承，并实现这个接口。
// 这里的两个方法在WorkflowActionBaseImpl中都有实现，缺省使用异步方式。
// 所以，如果Action是同步方式运行需要重写execute方法，调用WorkflowActionBaseImpl中的executeSync方法。
// QUES：对于异步动作，需要重点考虑该动作能不能在超时之后重新做？ 特别是与第三方外部系统打交到的动作
// 
public interface WorkflowAction
{
    public ActionResponse execute(final ModelWithWorkflowStep model, final String cid,
            final List<String> expectedResults);
}
