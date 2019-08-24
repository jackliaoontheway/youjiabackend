package com.polarj.workflow.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

public class StepAction implements Serializable
{
    private static final long serialVersionUID = 7489739476484347028L;

    private final @Getter String currentStep; // Status should inherit from a super enum with value {BEGIN, END}

    private @Getter @Setter String timeoutActionClass;

    private @Getter @Setter long timeoutSec;

    private @Getter @Setter String actionClass; // should be the fully qualified name of an implementation of
                                                // IWorkflowExternalAction interface

    private @Getter @Setter Integer priority;

    private @Getter @Setter String poolId;

    private @Getter @Setter Map<Serializable, String> nextStepMap; // object should be the response from action;

    private @Getter @Setter List<String> subSteps; // current step will be finished only if all the substeps are
                                                   // finished

    private @Getter @Setter String parentStep;

    private @Getter @Setter List<String> expectedResults;

    public StepAction(String step, String actionClass)
    {
        super();
        this.currentStep = step;
        this.actionClass = actionClass;
    }

    public boolean hasActionClass()
    {
        return !StringUtils.isBlank(this.getActionClass());
    }

    public boolean hasSubSteps()
    {
        List<String> steps = this.getSubSteps();
        if (steps == null || steps.size() == 0)
            return false;
        return true;
    }
}
