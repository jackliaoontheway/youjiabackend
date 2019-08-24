package com.polarj.workflow.action.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.polarj.common.CommonConstant;
import com.polarj.model.UserAccount;
import com.polarj.model.service.UserAccountService;
import com.polarj.workflow.model.ActionResponse;
import com.polarj.workflow.model.ModelWithWorkflowStep;

public class UserAccountOperationAction extends WorkflowActionBaseImpl
{
    @Autowired
    private UserAccountService service;

    private Map<String, String> wfStepToStatus;

    public UserAccountOperationAction()
    {
        wfStepToStatus = new HashMap<String, String>();
        wfStepToStatus.put("Pending", "PENDING");
        wfStepToStatus.put("Activing", "ACTIVE");
        wfStepToStatus.put("Locking", "LOCKED");
    }

    @Override
    protected void execute(ModelWithWorkflowStep model, ActionResponse res) throws Exception
    {
        UserAccount userAcc = service.getById(model.getId(), CommonConstant.defaultSystemLanguage);
        userAcc.setStatus(wfStepToStatus.get(userAcc.getWorkflowStep()));
        service.update(userAcc.getId(), userAcc, res.getProcessedBy(), CommonConstant.defaultSystemLanguage);
        TimeUnit.SECONDS.sleep(30);
        res.setResultEnum("OK");
    }

}
