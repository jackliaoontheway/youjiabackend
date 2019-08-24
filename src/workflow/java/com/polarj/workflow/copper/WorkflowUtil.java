package com.polarj.workflow.copper;

import org.copperengine.core.Acknowledge;
import org.copperengine.core.ProcessingEngine;
import org.copperengine.core.Response;

import com.polarj.workflow.exception.WorkflowRuntimeException;
import com.polarj.workflow.model.InterruptEvent;
import com.polarj.workflow.model.enumeration.InterruptEnum;

public final class WorkflowUtil
{
    public static final String INTERRUPT_PREFIX = "INTERRUPT-";

    /**
     * Use this utility method to return response to engine and resume workflow User might implement their own
     * Acknowledge for custom callback functions
     * 
     * @param response
     * @param ack
     */
    public static final void notifyResponse(ProcessingEngine engine, Response<?> response, Acknowledge ack)
    {
        if (engine == null)
        {
            throw new WorkflowRuntimeException("in notifyResponse, engine cannot be null", "", "");
        }
        engine.notify(response, ack);
    }

    /**
     * Use this utility method to send response to engine and resume workflow It will not return until engine
     * acknowledges
     * 
     * @param correlationId
     * @param R
     *            data, response.getResponse()
     */
    public static final <R> void notifyResponse(ProcessingEngine engine, String correlationId, R data, Exception e)
    {
        if (engine == null || correlationId == null)
        {
            throw new WorkflowRuntimeException("in notifyResponse, engine and correlationId cannot be null", "", "");
        }
        Response<R> response = new Response<>(correlationId, data, e);
        Acknowledge.DefaultAcknowledge defaultAck = new Acknowledge.DefaultAcknowledge();
        engine.notify(response, defaultAck);
        defaultAck.waitForAcknowledge();
    }

    public static <D> InterruptEvent<D> buildInterruptEvent(String instanceId, InterruptEnum interruptEnum,
            Object operator)
    {
        InterruptEvent<D> interruptEvent = new InterruptEvent<>(buildInterruptCid(instanceId), interruptEnum,
                operator != null ? operator.toString() : null);
        return interruptEvent;
    }

    public static String buildInterruptCid(String processId)
    {
        return INTERRUPT_PREFIX + processId;
    }

    public static String computeResultHash(String stepEnumFullName, String resultEnumFullName)
    {
        if (resultEnumFullName == null || resultEnumFullName.isEmpty())
        {
            return stepEnumFullName;
        }
        return stepEnumFullName + "-" + resultEnumFullName;
    }
}
