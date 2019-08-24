package com.polarj.workflow.exception;

/**
 * User defined exception to stop flow execution with the context and error message
 * 
 * @author Atomic
 *
 */
public class WorkflowTerminationException extends WorkflowException
{
    private static final long serialVersionUID = 1;

    public WorkflowTerminationException(Throwable cause, String context, String cid)
    {
        super(cause, context, cid);
    }

    public WorkflowTerminationException(String message, String context, String cid)
    {
        super(message, context, cid);
    }

}
