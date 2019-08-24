package com.polarj.workflow.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class WorkflowData implements Serializable
{
    private static final long serialVersionUID = -2347115623355589008L;

    private @Getter @Setter String wfName;

    private @Getter String modelSimpleName;

    private @Getter String modelServiceName;

    private @Getter @Setter ModelWithWorkflowStep model;

    private @Getter @Setter String firstStep;

    // 提交工作流的操作者。
    private @Getter @Setter Integer submittedBy;

    private @Getter @Setter Date submittedDate;

    private @Getter @Setter boolean cancellable;

    public WorkflowData(ModelWithWorkflowStep model, String modelServiceName,
            String wfName)
    {
        super();
        this.model = model;
        this.wfName = wfName;
        this.modelSimpleName = model.getClass().getSimpleName();
        this.modelServiceName = modelServiceName;
    }

    private @Getter @Setter Map<String, StepAction> stepActionMap;

    private @Getter @Setter Map<String, Integer> stepProgressMap;

    /**
     * specific poolId to run on, if null, will run on default pool
     */
    private @Getter @Setter String poolId = null;

    /**
     * if>0, will change to such priority on start
     */
    private @Getter @Setter Integer priority = null;

}
