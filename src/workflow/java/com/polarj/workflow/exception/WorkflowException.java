package com.polarj.workflow.exception;

import lombok.Getter;
import lombok.Setter;

public class WorkflowException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 7870002738883267600L;

    private @Getter @Setter String context;

    private @Getter @Setter String cid;

    public WorkflowException(String message)
    {
        super(message);

    }

    public WorkflowException(String message, String context, String cid)
    {
        super(message);
        this.cid = cid;
        this.context = context;
    }

    public WorkflowException(Throwable cause, String context, String cid)
    {
        super(cause);
        this.cid = cid;
        this.context = context;
    }

    public WorkflowException(String msg, Throwable cause, String context, String cid)
    {
        super(msg, cause);
        this.cid = cid;
        this.context = context;
    }
}
