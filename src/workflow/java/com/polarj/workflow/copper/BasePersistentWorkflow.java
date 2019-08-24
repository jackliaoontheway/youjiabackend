package com.polarj.workflow.copper;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.copperengine.core.Acknowledge;
import org.copperengine.core.AutoWire;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Response;
import org.copperengine.core.WaitMode;
import org.copperengine.core.audit.AuditTrail;
import org.copperengine.core.persistent.PersistentWorkflow;
import org.copperengine.core.util.Backchannel;
import org.copperengine.management.ProcessingEngineMXBean;
import org.copperengine.management.model.WorkflowInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polarj.common.CommonConstant;
import com.polarj.common.ResponseBase;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.service.EntityService;
import com.polarj.workflow.exception.WorkflowException;
import com.polarj.workflow.exception.WorkflowRuntimeException;
import com.polarj.workflow.exception.WorkflowTerminationException;
import com.polarj.workflow.model.InterruptEvent;
import com.polarj.workflow.model.ModelWithWorkflowStep;
import com.polarj.workflow.model.StepAction;
import com.polarj.workflow.model.WorkflowData;
import com.polarj.workflow.model.enumeration.InterruptEnum;

public abstract class BasePersistentWorkflow extends PersistentWorkflow<WorkflowData>
{

    private static final long serialVersionUID = -5414921076132557200L;

    private transient Backchannel workflowBackChannel;

    // 因为是在copper的容器内，所有使用copper定义的属性绑定
    @AutoWire
    public void setWorkflowBackChannel(Backchannel workflowBackChannel)
    {
        this.workflowBackChannel = workflowBackChannel;
    }

    protected static final int RUN_NOW = 1;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected enum LogLevel
    {
        INFO, WARN, ERROR
    }

    protected enum MessageType
    {
        TEXT, JSON, XML, BIN
    }

    protected transient AuditTrail auditTrail;

    @AutoWire
    public void setAuditTrail(AuditTrail auditTrail)
    {
        this.auditTrail = auditTrail;
    }

    protected abstract String processWorkflowStep(Map<String, StepAction> stepActionMap, String currentStep,
            Map<String, Integer> progressMap) throws Interrupt, WorkflowTerminationException;

    // protected abstract void runFlow() throws Interrupt, WorkflowTerminationException, WorkflowCancelException;

    protected abstract String getTransactionId();

    private String getWfName()
    {
        if (StringUtils.isNotBlank(getData().getWfName()))
        {
            return getData().getWfName();
        }
        return getClass().getSimpleName();
    }

    @Override
    public final void main() throws Interrupt
    {
        String log = getWfName() + "@" + getId() + " priority: " + getPriority() + " started";
        auditLog(LogLevel.INFO, getWfName(), log);

        // execute the main workflow
        try
        {
            WorkflowData wfData = getData();
            // start the first step
            auditLog(LogLevel.INFO, wfData.getFirstStep(),
                    "Running workflow " + wfData.getWfName() + " with model=" + wfData.getModel().toString());

            String currentStep = wfData.getFirstStep();
            Map<String, Integer> progressMap = wfData.getStepProgressMap();
            while (StringUtils.isNotEmpty(currentStep))
            {
                updateModelToCurrentStep(currentStep);
                currentStep = processWorkflowStep(wfData.getStepActionMap(), currentStep, progressMap);
            }
            log = getWfName() + "@" + getId() + " ended";
            auditLog(LogLevel.INFO, getWfName(), log);
            return;
        }
        catch (WorkflowException e)
        {
            logger.error(e.getMessage(), e);
            auditLog(LogLevel.ERROR, getWfName() + "-" + e.getContext(), e.getMessage(), e.getCid());
            return;
        }
        catch (RuntimeException e)
        {
            // unexpected like NPE, should be fixable
            logger.error(e.getMessage(), e);
            auditLog(LogLevel.ERROR, getWfName(), "Runtime exception: " + e.getMessage());
            throw e;
        }

    }

    @SuppressWarnings("unchecked")
    private boolean updateModelToCurrentStep(String currentStep)
    {
        ModelWithWorkflowStep model = getData().getModel();
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
            ModelWithWorkflowStep res = modelServie.getById(model.getId(), 5, CommonConstant.defaultSystemLanguage);
            res.setWorkflowStep(currentStep);
            res = modelServie.update(model.getId(), res, getData().getSubmittedBy(),
                    CommonConstant.defaultSystemLanguage);
            if (res != null)
            {
                getData().setModel(res);
                return true;
            }
        }
        return false;
    }

    /**
     * Put the workflow to sleep for specified seconds, save workflow state and release the thread
     * 
     * @param seconds
     * @throws Interrupt
     */
    protected final void sleepWithState(int seconds) throws Interrupt
    {
        logger.info("Sleeping " + seconds + " seconds up to next try...");
        wait(WaitMode.ALL, seconds * 1000, getEngine().createUUID());
    }

    protected String generateCorrelationId(WorkflowData wfData)
    {
        String correlationId = wfData.getWfName() + "-" + wfData.getModelSimpleName() + "-" + wfData.getModel().getId()
                + "-" + wfData.getModel().getWorkflowStep();
        return correlationId;
    }

    /**
     * Send a response to process engine without checking if anyone received it
     * 
     * @param correlationId
     * @param data
     *            - actual response data
     * @param metadata
     *            - extra message
     */
    protected <R> void notifyEngineAsync(String correlationId, R data, String metadata)
    {
        if (StringUtils.isEmpty(correlationId))
        {
            throw new IllegalArgumentException("correlationId cannot be null or empty");
        }
        Response<R> response = new Response<R>(correlationId, data, null, false, metadata, null, null);
        Acknowledge.BestEffortAcknowledge bestEffortAck = new Acknowledge.BestEffortAcknowledge();
        getEngine().notify(response, bestEffortAck);
    }

    /**
     * Send a response to process engine without checking if anyone received it
     * 
     * @param response
     *            - gives more control to the caller, use with care
     */
    protected <R> void notifyEngineAsync(Response<R> response)
    {
        Acknowledge.BestEffortAcknowledge bestEffortAck = new Acknowledge.BestEffortAcknowledge();
        getEngine().notify(response, bestEffortAck);
    }

    /**
     * Send a response to process engine and wait for its acknowledgement
     * 
     * @param correlationId
     * @param data
     *            - actual response data
     * @param metadata
     *            - extra message to save to response table
     */
    protected <R> void notifyEngineSync(String correlationId, R data, String metadata)
    {
        notifyEngineSync(correlationId, data, null, metadata);
    }

    /**
     * Send a response to process engine and wait for its acknowledgement
     * 
     * @param correlationId
     * @param data
     *            - actual response data
     * @param metadata
     *            - extra message to save to response table
     */
    protected <R> void notifyEngineSync(String correlationId, R data, Exception e, String metadata)
    {
        if (StringUtils.isEmpty(correlationId))
        {
            throw new IllegalArgumentException("correlationId cannot be null or empty");
        }
        Response<R> response = new Response<R>(correlationId, data, e, false, metadata, null, null);
        Acknowledge.DefaultAcknowledge defaultAck = new Acknowledge.DefaultAcknowledge();
        getEngine().notify(response, defaultAck);
        defaultAck.waitForAcknowledge();
    }

    /**
     * Use this when there could be multiple responses of the same correlationId
     * 
     * @param timeoutMs
     * @param correlationId
     * @return List of Responses for the waited correlationId, for timeout
     * @throws Interrupt
     */
    protected final <T> List<Response<T>> waitForResponses(long timeoutMs, String correlationId) throws Interrupt
    {
        if (StringUtils.isEmpty(correlationId))
        {
            throw new IllegalArgumentException("correlationId cannot be null or empty");
        }
        logger.info("Waiting for correlationId=[" + correlationId + "]'s notification");

        wait(WaitMode.ALL, timeoutMs, TimeUnit.MILLISECONDS, correlationId);

        List<Response<T>> resultData = getAndRemoveResponses(correlationId);
        if (resultData.isEmpty())
        {
            logger.info(correlationId + " returned empty list, probabably timeout");
        }
        else
        {
            logger.info(correlationId + " returned " + resultData.size() + " responses");
        }
        return resultData;
    }

    /**
     * Note: <b>Don't</b> Use this method when there could be multiple responses of the same correlationId at the same
     * time, use {@link #waitForResponses} instead
     * 
     * @param timeoutMs
     * @param correlationId
     * @return Response for the waited correlationId, check Response.isTimeout() for timeout
     * @throws Interrupt
     */
    protected final <T> Response<T> waitForResponse(long timeoutMs, String correlationId) throws Interrupt
    {
        return waitForResponse(timeoutMs, TimeUnit.MILLISECONDS, correlationId);
    }

    protected WorkflowData queryWorkflow()
    {
        return getData();
    }

    /**
     * 
     * @param timeout
     * @param timeUnit
     * @param correlationId
     * @return
     * @throws Interrupt
     * @throws WorkflowTerminationException
     */
    protected final <T> Response<T> waitForResponseWithInterrupt(long timeout, TimeUnit timeUnit, String correlationId)
            throws Interrupt, WorkflowTerminationException
    {
        if (StringUtils.isEmpty(correlationId))
        {
            throw new IllegalArgumentException("correlationId cannot be null or empty");
        }
        final String interruptCid = WorkflowUtil.buildInterruptCid(getId());

        logger.info("Waiting for correlationId=[" + correlationId + "]'s notification with interrupt");
        Response<T> resultData = null;
        boolean paused = false;
        long loopTimeout = timeout;
        for (;;)
        {
            if (paused)
            {
                loopTimeout = NO_TIMEOUT;
            }
            else
            {
                loopTimeout = timeout;
            }

            if (resultData == null)
            {
                wait(WaitMode.FIRST, loopTimeout, timeUnit, interruptCid, correlationId);
                resultData = getAndRemoveResponse(correlationId);
            }
            else if (paused)
            {
                wait(WaitMode.ALL, loopTimeout, timeUnit, interruptCid);
            }

            Response<InterruptEvent<Object>> intRes = getAndRemoveResponse(interruptCid);
            // Interrupt
            if (intRes != null && intRes.getResponse()!=null)
            {
                String context = "InterruptEvent";
                InterruptEvent<Object> intEvent = intRes.getResponse();
                InterruptEnum intEnum = intEvent.getInterruptEnum();
                switch (intEnum)
                {
                case PAUSE:
                    paused = true;
                    auditLog(LogLevel.INFO, context, "Workflow Paused");
                    break;
                case RESUME:
                    paused = false;
                    auditLog(LogLevel.INFO, context, "Workflow Resumed");
                    break;
                case QUERY:
                    Object queryRes = queryWorkflow();
                    workflowBackChannel.notify(intEvent.getSenderCId(), queryRes);
                    break;
                case TERMINATE:
                    throw new WorkflowTerminationException("Workflow termination signal received.", context,
                            interruptCid);
                case UPDATE:
                    updateWorkflow(intEvent.getUpdateRequest());
                    break;
                default:
                    throwRuntimeException("Unimplemented InterruptEnum " + intRes.getResponse(), context);
                }
            }
            else if (paused)
            {
                continue;
            }
            else
            {
                if (resultData.isTimeout())
                {
                    logger.info(correlationId + " response timeout");
                }
                else
                {
                    logger.info(correlationId + " returned with data " + resultData.getResponse());
                }
                break;
            }
        }
        return resultData;
    }

    /**
     * Note: <b>Don't</b> Use this method when there could be multiple responses of the same correlationId at the same
     * time, use {@link #waitForResponses} instead
     * 
     * @param timeoutMs
     * @param correlationId
     * @return Response for the waited correlationId, check Response.isTimeout() for timeout
     * @throws Interrupt
     */
    protected final <T> Response<T> waitForResponse(long timeout, TimeUnit timeUnit, String correlationId)
            throws Interrupt
    {
        if (StringUtils.isEmpty(correlationId))
        {
            throw new IllegalArgumentException("correlationId cannot be null or empty");
        }
        logger.info("Waiting for correlationId=[" + correlationId + "]'s notification");

        wait(WaitMode.ALL, timeout, timeUnit, correlationId);

        Response<T> resultData = getAndRemoveResponse(correlationId);
        if (resultData.isTimeout())
        {
            logger.info(correlationId + " response timeout");
        }
        else
        {
            logger.info(correlationId + " returned with data " + resultData.getResponse());
        }
        return resultData;
    }

    /**
     * 
     * @param timeoutSec
     * @param correlationId
     * @return List of the responses for the waited correlationId
     * @throws Interrupt
     *             protected final <T> List<Response<T>> waitForResponses(int timeoutSec, String correlationId) throws
     *             Interrupt { if (StringUtils.isEmpty(correlationId)) { throw new IllegalArgumentException(
     *             "correlationId cannot be null or empty"); } logger.info("Waiting for correlationId=[" + correlationId
     *             + "]'s notification");
     * 
     *             wait(WaitMode.ALL, timeoutSec * 1000, correlationId);
     * 
     *             List<Response<T>> resultData = getAndRemoveResponses(correlationId); logger.info(correlationId + "
     *             returned " + resultData.size() + " reponses"); return resultData; }
     */

    /**
     * This simply waits for all subprocesses to finish, without getting the responses for each
     * 
     * @param instances
     * @throws Interrupt
     */
    protected final void waitForAllSubflows(String... instances) throws Interrupt
    {
        if (instances == null || instances.length == 0)
        {
            throw new IllegalArgumentException("Subflow instances cannot be null or empty");

        }
        logger.info("Waiting for " + Arrays.toString(instances) + " to finish");

        waitForAll(instances);

        for (int i = 0; i < instances.length; i++)
        {
            getAndRemoveResponse(instances[i]);
        }
        logger.info("Subflows " + Arrays.toString(instances) + " finished");
    }

    /**
     * Minimum parameters to adding an auditlog, async style
     * 
     * @param level
     *            - see {@link LogLevel}
     * @param context
     *            - mandatory,maxLength=64 use it for the business step, like sendSms
     * @param message
     */
    protected void auditLog(LogLevel level, String context, String message)
    {
        auditLog(level, context, message, MessageType.TEXT, getEngine().createUUID(), null);
    }

    /**
     * Returns immediately after queuing the log message (async style) Typical parameters to adding an audit log NOTE:
     * log it only when needed
     * 
     * @param level
     *            - see {@link LogLevel}
     * @param context
     *            - mandatory,maxLength=64 use it for the business step, like sendSms
     * @param message
     * @param correlationId
     *            - optional, the id used for wait/notify
     */
    protected void auditLog(LogLevel level, String context, String message, String correlationId)
    {
        auditLog(level, context, message, MessageType.TEXT, getEngine().createUUID(), correlationId);
    }

    /**
     * Returns immediately after queuing the log message, full parameters NOTE: log it only when needed
     * 
     * @param level
     *            - mandatory
     * @param context
     *            - mandatory,maxLength=64, use it for the business step, like sendSms,
     * @param message
     *            - mandatory
     * @param messageType
     *            - default TEXT, see {@link MessageType}
     * @param conversationId
     *            - maxLength=128, default will be created using UUID
     * @param correlationId
     *            - maxLength=128, the id used for wait/notify default to null
     */
    protected void auditLog(LogLevel level, String context, String message, MessageType messageType,
            String conversationId, String correlationId)
    {

        if (level == null)
        {
            throw new IllegalArgumentException("LogLevel cannot be null");
        }
        if (context == null)
        {
            throw new IllegalArgumentException("context cannot be null");
        }
        if (conversationId == null)
        {
            conversationId = getEngine().createUUID();
        }
        if (messageType == null)
        {
            messageType = MessageType.TEXT;
        }
        switch (level)
        {
        case ERROR:
            logger.error(message);
            break;
        case INFO:
            logger.info(message);
            break;
        case WARN:
            logger.warn(message);
            break;
        default:
            logger.info(message);
            break;
        }
        auditTrail.synchLog(level.ordinal(), new Date(), conversationId, context, getId(), correlationId,
                getTransactionId(), message, messageType.name());

    }

    /**
     * Throw runtime exception to stop the workflow when unexpected exception occur
     * 
     * @param context
     * @param res
     * @param correlationId
     */
    protected void throwOnError(String context, ResponseBase<?> res, String correlationId)
    {
        if (res.hasError())
        {
            throwRuntimeException(res.toString(), context, correlationId);
        }
    }

    /**
     * Throw runtime exception to stop the workflow when unexpected exception occur
     * 
     * @param context
     * @param res
     */
    protected void throwOnError(String context, ResponseBase<?> res)
    {
        if (res.hasError())
        {
            throwRuntimeException("Error", context);
        }
    }

    protected void throwRuntimeException(String msg, String context)
    {
        throwRuntimeException(msg, context, null);
    }

    protected void throwRuntimeException(String msg, String context, String correlationId)
    {
        throw new WorkflowRuntimeException(msg, context, correlationId);
    }

    protected void throwRuntimeException(Throwable cause, String context, String correlationId)
    {
        throw new WorkflowRuntimeException(cause, context, correlationId);
    }

    protected boolean getBooleanValue(ResponseBase<Boolean> boolRes)
    {
        boolean res = (boolRes != null && boolRes.fetchOneData() != null && boolRes.fetchOneData().booleanValue());
        return res;
    }

    protected int getIntValue(ResponseBase<Integer> intRes)
    {
        if (intRes != null && intRes.fetchOneData() != null)
        {
            return intRes.fetchOneData();
        }
        return 0;
    }

    protected long getLongValue(ResponseBase<Long> longRes)
    {
        if (longRes != null && longRes.fetchOneData() != null)
        {
            return longRes.fetchOneData();
        }
        return 0;
    }

    protected boolean isWorkflowActive(final String workflowInstanceId)
    {
        WorkflowInfo wfi = getActiveWorkflow(workflowInstanceId);
        return wfi != null;
    }

    /**
     * internal use
     * 
     * @param workflowInstanceId
     * @return
     */
    protected WorkflowInfo getActiveWorkflow(final String workflowInstanceId)
    {
        ProcessingEngineMXBean engineMXBean = (ProcessingEngineMXBean) getEngine();
        WorkflowInfo wfi = engineMXBean.queryActiveWorkflowInstance(workflowInstanceId);
        return wfi;
    }

    protected void updateWorkflow(Object updateRequest)
    {
        return;
    }
}
