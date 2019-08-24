package com.polarj.workflow.model;

import lombok.Getter;
import lombok.Setter;

public class WorkflowValidationResult
{
    // 所需要校验的工作流是否有效
    private @Getter @Setter boolean pass;

    //
    private @Getter @Setter String message;

    // 工作流文字图型
    private @Getter @Setter String graph;
}
