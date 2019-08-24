package com.polarj.workflow.service.impl;

import java.util.Date;

import org.copperengine.core.Acknowledge;
import org.copperengine.core.ProcessingState;
import org.copperengine.core.Response;
import org.copperengine.core.WorkflowInstanceDescr;
import org.copperengine.management.model.WorkflowInfo;

import com.polarj.workflow.copper.PersistentWorkflow;
import com.polarj.workflow.copper.WorkflowUtil;
import com.polarj.workflow.model.ActionResponse;
import com.polarj.workflow.model.InterruptEvent;
import com.polarj.workflow.model.ModelWithWorkflowStep;
import com.polarj.workflow.model.WorkflowData;
import com.polarj.workflow.model.WorkflowResponse;
import com.polarj.workflow.model.enumeration.InterruptEnum;
import com.polarj.workflow.service.WorkflowService;

public class WorkflowServiceCopperImpl extends BaseServiceCopperImpl implements WorkflowService
{
    @Override
    final public WorkflowResponse<Boolean> isFinished(String processId)
    {
        WorkflowInfo wf = getActiveWorkflow(processId);
        WorkflowResponse<Boolean> res = new WorkflowResponse<Boolean>();
        res.addData(wf == null || wf.getState().equals(ProcessingState.FINISHED.name()));
        return res;
    }

    @Override
    final public WorkflowResponse<Boolean> isCancelleable(String processId)
    {
        WorkflowResponse<Boolean> res = new WorkflowResponse<Boolean>();
        res = isCancelleable(processId, res);
        return res;
    }

    private WorkflowResponse<Boolean> isCancelleable(String processId, WorkflowResponse<Boolean> res)
    {
        WorkflowData wfData = queryProcessData(processId, res);
        if (!res.hasError())
        {
            res.addData(wfData.isCancellable());
        }

        return res;
    }

    @Override
    final public WorkflowResponse<String> startProcess(ModelWithWorkflowStep model,
            String modelServiceName, String wfName, Integer operator)
    {
        WorkflowDataContent content = new WorkflowDataContent(wfName);
        WorkflowResponse<String> res = new WorkflowResponse<String>();
        WorkflowData data = new WorkflowData(model, modelServiceName, wfName);
        data.setStepActionMap(content.stepActionMap);
        data.setStepProgressMap(content.stepProgressMap);
        data.setFirstStep(content.firstStep);
        if (operator != null)
        {
            data.setSubmittedBy(operator);
        }
        data.setSubmittedDate(new Date());

        try
        {
        	//把工作流实例ID跟定单关联
            WorkflowInstanceDescr<WorkflowData> wfInstanceDescr = new WorkflowInstanceDescr<WorkflowData>(
                    PersistentWorkflow.WORKFLOW_NAME, data, model.getWfPid(), content.priority, content.poolId);

            // workflow instance Id
            String processId = engine.run(wfInstanceDescr);

            res.addData(processId);

        }
        catch (Exception e)
        {
            res.addStatus(exception(e.getMessage()));
            logger.error(e.getMessage(), e);
        }
        return res;
    }

    @Override
    final public WorkflowResponse<Boolean> suspendProcess(String processId, Integer operator)
    {
        final InterruptEnum interruptEnum = InterruptEnum.PAUSE;
        InterruptEvent<Void> interruptEvent = WorkflowUtil.buildInterruptEvent(processId, interruptEnum, operator);
        return notifyEngineSync(processId, interruptEvent.getCorrelationId(), interruptEvent, null);
    }

    @Override
    final public WorkflowResponse<Boolean> resumeProcess(String processId, Integer operator)
    {
        final InterruptEnum interruptEnum = InterruptEnum.RESUME;
        InterruptEvent<Void> interruptEvent = WorkflowUtil.buildInterruptEvent(processId, interruptEnum, operator);
        return notifyEngineSync(processId, interruptEvent.getCorrelationId(), interruptEvent, null);
    }

    @Override
    final public WorkflowResponse<Void> notifyEngineWithAsyncActionResult(ModelWithWorkflowStep model, String result,
            String wfName, Integer operId)
    {
        String correlationId =
                wfName + "-" + model.getEntityName() + "-" + model.getId() + "-" + model.getWorkflowStep();
        ActionResponse response = new ActionResponse(correlationId);
        response.setResultEnum(result);
        Response<ActionResponse> engineResponse =
                new Response<ActionResponse>(correlationId, response, null, false, null, null, null);
        Acknowledge.DefaultAcknowledge defaultAck = new Acknowledge.DefaultAcknowledge();
        engine.notify(engineResponse, defaultAck);
        defaultAck.waitForAcknowledge();
        return new WorkflowResponse<Void>();
    }

}
