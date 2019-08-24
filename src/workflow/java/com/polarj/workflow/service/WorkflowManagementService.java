package com.polarj.workflow.service;

import java.util.List;

import com.polarj.workflow.model.WorkflowResponse;
import com.polarj.workflow.model.WorkflowValidationResult;
import com.polarj.workflow.model.enumeration.WorkflowState;

// 用于实现工作流的管理功能。
public interface WorkflowManagementService
{
    /**
     * 獲取process裡面的data, JSON 格式的String
     * @param processId
     * @return
     */
    public WorkflowResponse<String> getProcessData(String processId);

    /**
     * 驗證workflow是否有效
     * @param wfName
     * @param graph - 是否繪圖, 默認不返回
     * @return
     */
    public WorkflowResponse<WorkflowValidationResult> validateWorkflow(String wfName, boolean graph);

    /**
     * 驗證workflow是否有效
     * @param wfName
     * @return
     */
    public WorkflowResponse<WorkflowValidationResult> validateWorkflow(String wfName);

    /**
     * 把workflow繪製出來,必須使用Courier New字體
     * @param wfName
     * @return
     */
    public WorkflowResponse<String> graphWorkflow(String wfName);

    /**
     * 把workflow繪製出來,必須使用Courier New字體
     * @param wfName
     * @param from - 從指定位置繪製
     * @return
     */
    public WorkflowResponse<String> graphWorkflowFrom(String wfName, String fromStep);

    /**
     * 終止流程, 如果此流程包含子工作流則所有的子工作流也會被終止 
     * @param processId
     * @param note
     * @param operator
     */
    public WorkflowResponse<Boolean> terminateProcess(String processId, String note, Integer operator);

    public WorkflowResponse<Boolean> isProcessActive(String processId);

    public WorkflowResponse<WorkflowState> getProcessState(final String schedulerInstanceId);

    /**
     * 找到一個process所有正在等待的cid
     * @param processId
     * @return
     */
    public WorkflowResponse<String> listCidByProcessId(String processId);

    /**
     * 找到一個cid對應的processId
     * @param cid
     * @return
     */
    public WorkflowResponse<String> getProcessIdByCid(String cid);

    public WorkflowResponse<Boolean> restartErrorProcesses(List<String> processIds, Integer oper);
}
