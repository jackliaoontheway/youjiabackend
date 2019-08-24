package com.polarj.common.web.service;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.polarj.common.CommonConstant;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.service.EntityService;
import com.polarj.workflow.model.ModelWithWorkflowStep;
import com.polarj.workflow.service.WorkflowService;

// 与前端接口过程中，如果需要使用到工作流的服务，就直接使用这个service里面的操作
public class WorkflowViewService
{
    @Autowired(required = false)
    protected WorkflowService workflowService;

    @SuppressWarnings("unchecked")
    public <M extends GenericDbInfo, ID extends Serializable, S extends EntityService<M, ID>> void
            workflowForCreatingModel(M result, S service, Integer operId)
    {
        if (result == null || !ModelWithWorkflowStep.class.isAssignableFrom(result.getClass()))
        {
            return;
        }
        ((ModelWithWorkflowStep) result).setWorkflowStep("NEW");
        service.update((ID) result.getId(), result, operId, CommonConstant.defaultSystemLanguage);
        String wfName = getWorkflowName(result);
        String modelSeriveName = service.getClass().getName();
        if (workflowService != null && StringUtils.isNotEmpty(wfName) && StringUtils.isNotEmpty(modelSeriveName))
        {
            workflowService.startProcess((ModelWithWorkflowStep) result, modelSeriveName, wfName, operId);
        }
    }

    public <M extends GenericDbInfo> void workflowForUserOperation(M result, String actionString, Integer operId)
    {
        if (result == null || !ModelWithWorkflowStep.class.isAssignableFrom(result.getClass()))
        {
            return;
        }
        String wfName = getWorkflowName(result);
        if (workflowService != null && StringUtils.isNotEmpty(wfName))
        {
            workflowService.notifyEngineWithAsyncActionResult((ModelWithWorkflowStep) result, actionString, wfName,
                    operId);
        }
    }

    public <M extends GenericDbInfo> void workflowForScheduler(M result, String nextStepCode, Integer operId)
    {
        workflowForUserOperation(result, nextStepCode, operId);
    }

    // QUES: 管理业务模型的工作流需要从业务模型的持久化数据中获取。
    // 目前是从业务模型的说明中得到的
    private <M extends GenericDbInfo> String getWorkflowName(M result)
    {
        String wfName = "";
        ModelMetaData mmd = result.getClass().getAnnotation(ModelMetaData.class);
        if (mmd != null && mmd.workflowName().length > 0)
        {
            wfName = mmd.workflowName()[0];
        }
        return wfName;
    }
}
