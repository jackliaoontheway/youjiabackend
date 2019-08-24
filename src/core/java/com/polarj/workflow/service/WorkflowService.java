package com.polarj.workflow.service;

import com.polarj.workflow.model.ModelWithWorkflowStep;
import com.polarj.workflow.model.WorkflowResponse;

// 工作流的操作服务接口
// 各个方法中的 processId 是startProcess的返回值
public interface WorkflowService
{
    public WorkflowResponse<Boolean> isFinished(String processId);

    public WorkflowResponse<Boolean> isCancelleable(String processId);

    public WorkflowResponse<Boolean> isProcessActive(String processId);

    // 返回值是工作流引擎标识工作流实例的全局唯一id。
    // 目前是类org.copperengine.core.WorkFlow的属性id的值
    // 也就是workflow的instance id，该值在copper引擎运行该工作流实例时，可以被自动生成
    public WorkflowResponse<String> startProcess(ModelWithWorkflowStep model, String modelServiceName, String wfName,
            Integer operator);

    public WorkflowResponse<Void> notifyEngineWithAsyncActionResult(ModelWithWorkflowStep model, String result,
            String wfName, Integer operator);

    public WorkflowResponse<Boolean> suspendProcess(String processId, Integer operator);

    public WorkflowResponse<Boolean> resumeProcess(String processId, Integer operator);
}
