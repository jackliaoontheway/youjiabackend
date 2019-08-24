package com.polarj.workflow.copper;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Response;
import org.copperengine.core.WorkflowDescription;
import org.springframework.beans.BeansException;

import com.polarj.common.CommonConstant;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.service.EntityService;
import com.polarj.workflow.TimeoutAction;
import com.polarj.workflow.action.WorkflowAction;
import com.polarj.workflow.action.WorkflowTimeoutAction;
import com.polarj.workflow.exception.WorkflowTerminationException;
import com.polarj.workflow.model.ActionResponse;
import com.polarj.workflow.model.ModelWithWorkflowStep;
import com.polarj.workflow.model.StepAction;
import com.polarj.workflow.model.WorkflowData;

@WorkflowDescription(alias = PersistentWorkflow.WORKFLOW_NAME, majorVersion = 1, minorVersion = 0,
        patchLevelVersion = 0)
public class PersistentWorkflow extends BasePersistentWorkflow
{
    private static final long serialVersionUID = 156683576057591445L;

    public static final String WORKFLOW_NAME = "PersistentWorkflow";

    private transient WorkflowAction workflowExternalAction;

    private transient WorkflowTimeoutAction workflowTimeoutAction;

    protected String processWorkflowStep(Map<String, StepAction> stepActionMap, String currentStep,
                                         Map<String, Integer> progressMap) throws Interrupt, WorkflowTerminationException
    {
        WorkflowData wfData = getData();
        StepAction stepAction = stepActionMap.get(currentStep);

        if (null == stepAction)
        {
            // No action defined
            String message =
                    "Workflow Ended, name=" + wfData.getWfName() + ", step=" + currentStep + " stepAction is null";
            auditLog(LogLevel.WARN, currentStep, message);
            return null;
        }

        String stepEnumName = currentStep;
        if (!currentStep.equals(stepAction.getCurrentStep()))
        {
            throwRuntimeException("Flow " + wfData.getModelSimpleName() + " expected " + stepAction.getCurrentStep()
                    + " but got runtime status: " + stepEnumName, stepEnumName);
            return null;
        }

        // TODO reload wfData's model?

        // fork substeps if any

        // execute action and find next step using result
        String triggerEventHash;
        String triggerStepHash = WorkflowUtil.computeResultHash(currentStep, null);
        // execute action
        ActionResponse result = null;
        if (stepAction.hasActionClass())
        {
            while (result == null || result.isRedoCurAction())
            {
                result = executeAction(wfData, stepAction);
            }
            // compute hash using currentStep + result
            triggerEventHash = WorkflowUtil.computeResultHash(currentStep, result.getResultEnum());

        }
        else
        {
            // 標準流程沒有action, 不允許
            throwRuntimeException("Invalid standard step for flow " + wfData.getWfName() + " step=" + stepEnumName
                    + " actionClass is empty!", stepEnumName);
            return null;
        }
        Map<Serializable, String> nextMap = stepAction.getNextStepMap();

        String nextStep = nextMap.get(triggerEventHash);
        if (null == nextStep && !triggerEventHash.equals(triggerStepHash))
        {
            // try triggerStepHash
            nextStep = nextMap.get(triggerStepHash);
        }

        if (null == nextStep)
        {
            String message = "Workflow Ended, triggerEvent=" + triggerEventHash + "-> nextStep is undefined";
            auditLog(LogLevel.INFO, wfData.getWfName(), message);
            wfData.getModel().setWorkflowStep(result.getResultEnum());
            updateModel(wfData.getModel());
            return null;
        }
        // update progress meter
        progressMap.put(currentStep, progressMap.get(currentStep) + 1);
        // next step
        setData(wfData);
        return nextStep;
    }

    @SuppressWarnings("unchecked")
    private void updateModel(ModelWithWorkflowStep model)
    {
        EntityService<ModelWithWorkflowStep, Integer> modelServie = null;
        try
        {
            modelServie = (EntityService<ModelWithWorkflowStep, Integer>) SpringContextUtils
                    .getBean(getData().getModelServiceName());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            modelServie = null;
        }
        if (modelServie != null)
        {
        	//从数据库拿到最新model作更新
        	model = modelServie.getById(model.getId(), CommonConstant.defaultSystemLanguage);
            model = modelServie.update(model.getId(), model, getData().getSubmittedBy(), CommonConstant.defaultSystemLanguage);
            WorkflowData wfData = getData();
            wfData.setModel(model);
        }
    }

    private ActionResponse executeAction(WorkflowData wfData, StepAction stepAction)
            throws Interrupt, WorkflowTerminationException
    {
        String currentStep = stepAction.getCurrentStep();
        String actionClass = stepAction.getActionClass();

        // execute external action
        ModelWithWorkflowStep model = wfData.getModel();
        ActionResponse ack = null;
        try
        {
            auditLog(LogLevel.INFO, currentStep, "try executeAction [" + actionClass + "]");
            workflowExternalAction = (WorkflowAction) SpringContextUtils.getBean(actionClass);
            if (workflowExternalAction == null)
            {
                throwRuntimeException("Flow " + wfData.getWfName() + "'s action for step=" + currentStep + " is null",
                        currentStep);
            }
            ack = workflowExternalAction.execute(model, generateCorrelationId(wfData), stepAction.getExpectedResults());
            if (ack == null)
            {
                throwRuntimeException(
                        "Flow " + wfData.getWfName() + " step=" + currentStep + " return actionAck is null",
                        currentStep);
            }
        }
        catch (BeansException e)
        {
            throwRuntimeException(
                    "Flow " + wfData.getWfName() + " step=" + currentStep + " invalid actionClass=" + actionClass,
                    currentStep);
        }

        // copper wait on step event with CID
        final String cid = ack.getCorrelationId();
        auditLog(LogLevel.INFO, currentStep, "executed action [" + actionClass + "], waiting=" + ack.isAsync(), cid);

        // 設為是否能被取消
        if (ack.getFlowCancellable() != null)
        {
            wfData.setCancellable(ack.getFlowCancellable());
        }

        ActionResponse result;

        if (ack.isAsync())
        {
            // async should always come here at this moment
            result = waitForNotification(currentStep, stepAction, cid);
        }
        else
        {
            // sync
            result = ack;
        }
        updateModel(model);

        // logging
        auditLog(LogLevel.INFO, currentStep, "action completed with result=" + result.getResultEnum(), cid);
        return result;
    }

    private ActionResponse waitForNotification(String currentStep, StepAction stepAction, String cid)
            throws Interrupt, WorkflowTerminationException
    {
        Response<ActionResponse> response = null;
        TimeoutAction res = null;
        while (res == null || res == TimeoutAction.WAITING)
        {
            response = waitForResponseWithInterrupt(stepAction.getTimeoutSec(), TimeUnit.SECONDS, cid);
            if (response.getException() != null)
            {
                // 如果有exception，应该需要系统人员干预处理
                handleNotifierException(stepAction, response, response.getException());
            }
            else if (response.isTimeout())
            {
                // 如果是超时，一个是执行超时要做的动作，二个就是考虑继续等待或者重做当前动作（要考虑动作重做是不是有害！）
                res = handleTimeout(stepAction);
                if (res == TimeoutAction.REDOACTION)
                {
                    ActionResponse actionRes = new ActionResponse(cid);
                    actionRes.setRedoCurAction(true);
                    return actionRes;
                }
            }
            else
            {
                // TODO: refresh data & model
                // setData(...);
                break;
            }

        }
        return response.getResponse();

    }

    private TimeoutAction handleTimeout(StepAction stepAction)
    {
        String timeoutActionClass = stepAction.getTimeoutActionClass();
        String currentStep = stepAction.getCurrentStep();
        if (StringUtils.isNotBlank(timeoutActionClass))
        {
            try
            {
                // execute external action
                workflowTimeoutAction = (WorkflowTimeoutAction) SpringContextUtils.getBean(timeoutActionClass);
                WorkflowData data = getData();
                // 因为copper容器启动之后，其他的配置可能还没有准备好，所以，需要做NPE判断，当没有准备好的时候，需要继续等待。
                if (data != null && workflowTimeoutAction != null)
                {
                    TimeoutAction res = workflowTimeoutAction.execute(data.getModel(), currentStep);
                    return res;
                }
                else
                {
                    auditLog(LogLevel.ERROR, currentStep, "No workflow data or action class ready for ", currentStep);
                }
                // wait again
            }
            catch (BeansException e)
            {
                throwRuntimeException("Flow " + currentStep + " step=" + currentStep + " invalid timeoutActionClass="
                        + timeoutActionClass, currentStep);
            }
        }
        return TimeoutAction.WAITING;
    }

    private void handleNotifierException(StepAction stepAction, Response<ActionResponse> response, Exception exception)
    {
        // rethrow all other exceptions
        throwRuntimeException(response.getException(), stepAction.getCurrentStep(), response.getCorrelationId());
    }

    @Override
    protected String getTransactionId()
    {
        String tid = "";
        if(getData()==null)
        {
            tid = "workflow data is null.";
        }
        else
        {
            if(getData().getModel()==null)
            {
                tid = "model data for workflow is null.";
            }
            else
            {
                tid = getData().getWfName() + "-" + getData().getModel().toString();
            }
        }
        // 如果id過長則截斷
        if (tid.length() > 128)
        {
            tid = tid.substring(0, 126) + "..";
        }
        return tid;
    }
}
