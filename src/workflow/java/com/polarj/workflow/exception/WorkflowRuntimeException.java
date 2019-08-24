package com.polarj.workflow.exception;

import lombok.Getter;
import lombok.Setter;

public class WorkflowRuntimeException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 6375465994790648956L;

    private @Getter @Setter String context;

    private @Getter @Setter String cid;

    public WorkflowRuntimeException(String message)
    {
        super(message);
        this.cid = "";
        this.context = "";
    }

    public WorkflowRuntimeException(Throwable cause, String context, String cid)
    {
        super(cause);
        this.cid = cid;
        this.context = context;
    }

    public WorkflowRuntimeException(String message, String context, String cid)
    {
        super(message);
        this.cid = cid;
        this.context = context;
    }
}
