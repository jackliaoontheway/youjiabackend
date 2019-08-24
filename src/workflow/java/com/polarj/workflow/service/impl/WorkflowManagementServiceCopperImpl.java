package com.polarj.workflow.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.copperengine.management.model.WorkflowInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarj.workflow.copper.WorkflowUtil;
import com.polarj.workflow.model.CopperWait;
import com.polarj.workflow.model.InterruptEvent;
import com.polarj.workflow.model.WorkflowData;
import com.polarj.workflow.model.WorkflowResponse;
import com.polarj.workflow.model.WorkflowValidationResult;
import com.polarj.workflow.model.enumeration.InterruptEnum;
import com.polarj.workflow.model.enumeration.WorkflowState;
import com.polarj.workflow.model.repository.CopperWaitRepos;
import com.polarj.workflow.service.WorkflowManagementService;

import lombok.Setter;

public class WorkflowManagementServiceCopperImpl extends BaseServiceCopperImpl implements WorkflowManagementService
{
    private static ObjectMapper objectMapper = new ObjectMapper();

    private @Setter CopperWaitRepos copperWaitRepos;

    @Override
    public WorkflowResponse<String> getProcessData(String processId)
    {
        WorkflowResponse<String> res = new WorkflowResponse<String>();
        WorkflowData processData = queryProcessData(processId, res);
        if (!res.hasError())
        {
            String s = null;
            try
            {
                s = objectMapper.writeValueAsString(processData);
                if (s == null || s.isEmpty())
                {
                    res.addStatus(unknowError("write process data to JSON error for: " + processData.getWfName()
                            + " with " + processData.getModelSimpleName()));
                }
                else
                {
                    res.addData(s);
                }
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
                res.addStatus(exception(e.getMessage()));
            }
        }
        return res;
    }

    @Override
    public WorkflowResponse<WorkflowValidationResult> validateWorkflow(String wfName, boolean graph)
    {
        WorkflowResponse<WorkflowValidationResult> res = new WorkflowResponse<WorkflowValidationResult>();
        WorkflowDataContent content = new WorkflowDataContent(wfName);
        res.addData(content.validate(graph));
        return res;
    }

    @Override
    public WorkflowResponse<WorkflowValidationResult> validateWorkflow(String wfName)
    {
        return validateWorkflow(wfName, false);
    }

    @Override
    public WorkflowResponse<String> graphWorkflow(String wfName)
    {
        WorkflowResponse<String> res = new WorkflowResponse<String>();
        WorkflowDataContent content = new WorkflowDataContent(wfName);
        res.addData(content.graphWorkflow());
        return res;
    }

    @Override
    public WorkflowResponse<String> graphWorkflowFrom(String wfName, String fromStep)
    {
        WorkflowResponse<String> res = new WorkflowResponse<String>();
        WorkflowDataContent content = new WorkflowDataContent(wfName);
        res.addData(content.graphWorkflow(fromStep));
        return res;
    }

    @Override
    public WorkflowResponse<Boolean> terminateProcess(String processId, String note, Integer operator)
    {
        final InterruptEnum interruptEnum = InterruptEnum.TERMINATE;
        InterruptEvent<Void> interruptEvent = WorkflowUtil.buildInterruptEvent(processId, interruptEnum, operator);
        WorkflowResponse<Boolean> res =
                notifyEngineSync(processId, interruptEvent.getCorrelationId(), interruptEvent, null);
        return res;
    }

    @Override
    public WorkflowResponse<WorkflowState> getProcessState(String schedulerInstanceId)
    {
        waitUntilEngineIsStarted();
        WorkflowInfo wfi = getActiveWorkflow(schedulerInstanceId);
        final WorkflowState status;
        if (wfi == null)
        {
            status = WorkflowState.NOTFOUND;
        }
        else
        {
            status = WorkflowState.valueOf(wfi.getState());
        }
        WorkflowResponse<WorkflowState> res = new WorkflowResponse<WorkflowState>();
        res.addData(status);
        return res;
    }

    @Override
    public WorkflowResponse<String> listCidByProcessId(String processId)
    {
        WorkflowResponse<String> res = new WorkflowResponse<String>();
        List<String> waitingCorrelationIds = new ArrayList<String>();
        List<CopperWait> waits = copperWaitRepos.findByWorkflowInstanceId(processId);
        if (waits == null)
        {
            res.addStatus(unknowError("Can not find Process for WorkflowInstanceId: " + processId));
            return res;
        }
        for (CopperWait wait : waits)
        {
            // 剔除INTERRUPT開頭的
            if (wait.getCorrelationId().startsWith(WorkflowUtil.INTERRUPT_PREFIX))
            {
                continue;
            }
            waitingCorrelationIds.add(wait.getCorrelationId());
            res.addDatas(waitingCorrelationIds);
        }
        return res;
    }

    @Override
    public WorkflowResponse<String> getProcessIdByCid(String cid)
    {
        WorkflowResponse<String> res = new WorkflowResponse<String>();
        CopperWait copperWait = copperWaitRepos.getOne(cid);
        if (copperWait == null)
        {
            res.addStatus(unknowError("Can not find Process for CorrelationId: " + cid));
        }
        else
        {
            res.addData(copperWait.getWorkflowInstanceId());
        }
        return res;
    }

    @Override
    public WorkflowResponse<Boolean> restartErrorProcesses(List<String> processIds, Integer oper)
    {
        WorkflowResponse<Boolean> res = new WorkflowResponse<Boolean>();
        boolean allSuccess = true;
        if (processIds == null)
        {
            res.addStatus(unknowError("processIds cannot be empty or null!"));
            return res;
        }
        for (String processId : processIds)
        {
            try
            {
                engine.restart(processId);
            }
            catch (Exception e)
            {
                logger.error("Fail to restart process id=" + processId, e);
                res.addStatus(exception(e.getMessage() + " for process id=" + processId));
                allSuccess = false;
            }
            logger.info("Restart process id=" + processIds + " command issued, please wait");
        }
        res.addData(allSuccess);
        return res;
    }

}
