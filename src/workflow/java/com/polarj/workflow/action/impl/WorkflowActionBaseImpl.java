package com.polarj.workflow.action.impl;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.copperengine.core.ProcessingEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polarj.workflow.action.WorkflowAction;
import com.polarj.workflow.copper.WorkflowUtil;
import com.polarj.workflow.model.ActionResponse;
import com.polarj.workflow.model.ModelWithWorkflowStep;

import lombok.Setter;

// action的copper实现基类，
// 如果action是需要异步操作的，就继承该类，实现里面的抽象方法，
// 如果是同步的action，就继承WorkflowSyncActionBaseImpl类， 实现同样的抽象方法。 
public abstract class WorkflowActionBaseImpl implements WorkflowAction
{
    protected static final Random random = new Random();

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private @Setter ProcessingEngine engine;

    @Override
    public ActionResponse execute(ModelWithWorkflowStep model, String cid, List<String> expectedResults)
    { 
        return executeAsync(model, cid, expectedResults);
    }

    final protected ActionResponse executeAsync(ModelWithWorkflowStep model, String cid, List<String> expectedResults)
    {
        ActionResponse res = new ActionResponse(cid);
        res.setExpectedResults(expectedResults);
        Runnable r = new Runnable()
        {
            public void run()
            {
                String resultStatus = "ERROR";
                Exception actionException = null;
                try
                {
                    execute(model, res);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                    res.setResultEnum(resultStatus);
                    actionException = e;
                }
                if(!res.isManullyNotification())
                {
                    notifyEngine(res, actionException);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        return res;
    }

    final protected ActionResponse executeSync(ModelWithWorkflowStep model, String cid, List<String> expectedResults)
    {
        ActionResponse res = new ActionResponse(cid);
        String resultStatus = "ERROR";
        res.setResultEnum(resultStatus);
        res.setAsync(false);
        try
        {
            execute(model, res);
        }
        catch (Exception e)
        {
            res.setResultEnum(resultStatus);
            logger.error(e.getMessage(), e);
        }
        return res;
    }

    abstract protected void execute(ModelWithWorkflowStep model, ActionResponse res) throws Exception;

    private void notifyEngine(ActionResponse actionResponse, Exception exception)
    {
        WorkflowUtil.notifyResponse(engine, actionResponse.getCorrelationId(), actionResponse, exception);
    }

    protected String getContext()
    {
        return getClassName() + "." + getCurrentMethodName();
    }

    protected String getClassName()
    {
        return this.getClass().getSimpleName();
    }

    protected static String getCurrentMethodName()
    {
        return new Throwable().getStackTrace()[2].getMethodName();
    }
}
