package com.polarj.workflow.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.copperengine.core.Acknowledge;
import org.copperengine.core.EngineState;
import org.copperengine.core.PersistentProcessingEngine;
import org.copperengine.core.Response;
import org.copperengine.core.util.Backchannel;
import org.copperengine.management.ProcessingEngineMXBean;
import org.copperengine.management.model.WorkflowInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polarj.common.CommonConstant;
import com.polarj.workflow.copper.WorkflowUtil;
import com.polarj.workflow.exception.WorkflowRuntimeException;
import com.polarj.workflow.model.InterruptEvent;
import com.polarj.workflow.model.StepAction;
import com.polarj.workflow.model.WorkflowData;
import com.polarj.workflow.model.WorkflowError;
import com.polarj.workflow.model.WorkflowResponse;
import com.polarj.workflow.model.WorkflowSpec;
import com.polarj.workflow.model.WorkflowStep;
import com.polarj.workflow.model.WorkflowValidationResult;
import com.polarj.workflow.model.enumeration.InterruptEnum;
import com.polarj.workflow.model.service.WorkflowSpecService;

import lombok.Setter;

public abstract class BaseServiceCopperImpl
{
    protected @Setter PersistentProcessingEngine engine;

    protected @Setter WorkflowSpecService wfSpecService;

    protected @Setter Backchannel workflowBackChannel;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final WorkflowError unknowError(String msg)
    {
        return new WorkflowError("unknow.error", msg);
    }

    protected final WorkflowError error(String code, String msg)
    {
        return new WorkflowError("error." + code, msg);
    }

    protected final WorkflowError exception(String msg)
    {
        return new WorkflowError("exception", msg);
    }

    protected String defaultLanguageCode = CommonConstant.defaultSystemLanguage;

    /**
     * internal use
     * 
     * @param schedulerInstanceId
     * @return
     */
    protected WorkflowInfo getActiveWorkflow(final String processId)
    {
        waitUntilEngineIsStarted();
        ProcessingEngineMXBean engineMXBean = (ProcessingEngineMXBean) engine;
        WorkflowInfo wfi = engineMXBean.queryActiveWorkflowInstance(processId);
        return wfi;
    }

    public final WorkflowResponse<Boolean> isProcessActive(String processId)
    {
        WorkflowInfo wfi = getActiveWorkflow(processId);
        WorkflowResponse<Boolean> res = new WorkflowResponse<Boolean>();
        res.addData(wfi != null);
        return res;
    }

    /**
     * Send a response to process engine and wait for its acknowledgement
     * 
     * @param cid
     * @param data
     *            - actual response data
     * @param metadata
     *            - extra message to save to response table
     */
    protected <R, T> WorkflowResponse<Boolean> notifyEngineSync(final String processId, final String cid, final R data,
            final String metadata)
    {
        WorkflowResponse<Boolean> res = new WorkflowResponse<Boolean>();
        if (StringUtils.isEmpty(processId) || StringUtils.isEmpty(cid))
        {
            res.addStatus(unknowError("processId && cid cannot be null or empty"));
            throw new WorkflowRuntimeException("processId && cid cannot be null or empty");
        }
        if (!isProcessActive(processId).fetchOneData())
        {
            res.addStatus(unknowError("wfid=" + processId + " is not active"));
            throw new WorkflowRuntimeException("wfid=" + processId + " is not active");
        }

        Response<R> response = new Response<R>(cid, data, null, false, metadata, null, null);
        try
        {
            Acknowledge.DefaultAcknowledge defaultAck = new Acknowledge.DefaultAcknowledge();
            engine.notify(response, defaultAck);
            defaultAck.waitForAcknowledge();
            Thread.sleep(100);
            res.addData(true);
            return res;
        }
        catch (InterruptedException e)
        {
            res.addStatus(exception(e.getMessage() + " for wfid=" + processId + ", data=" + data));
            throw new WorkflowRuntimeException("wfid=" + processId + ", data=" + data);
        }
    }

    protected void waitUntilEngineIsStarted()
    {
        while (EngineState.STARTED != engine.getEngineState())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private String populateString(String stepName)
    {
        if (StringUtils.isBlank(stepName))
        {
            return null;
        }
        return stepName;
    }

    protected class WorkflowDataContent
    {
        /**
         * Step -> StepAction
         */
        Map<String, StepAction> stepActionMap = new HashMap<>();

        Map<String, Integer> stepProgressMap = new HashMap<>();

        // private Map<String, String[]> stepResults = new HashMap<>();
        String firstStep = null;

        Integer priority = null;

        String poolId = null;

        int totalSteps;

        final String wfName;

        WorkflowDataContent(String wfName)
        {
            this.wfName = wfName;
            loadContent(wfName);
        }

        protected void loadContent(String wfName)
        {
            /**
             * resultEvent->step
             * 
             */
            Map<Serializable, String> trigger2StepMap = new HashMap<>();

            /**
             * Step -> substeps
             */
            Map<String, List<String>> subStepsMap = new HashMap<>();

            // load from db
            WorkflowSpec wf = wfSpecService.fetchLatestWorkflow(wfName);
            List<WorkflowStep> steps = wf.getSteps();
            if (steps == null || steps.isEmpty())
            {
                logger.error("Cannot find any steps by name=" + wfName);
                return;
            }
            totalSteps = steps.size();

            // parse #0
            String parentStepCode = wf.getParentStepCode();
            for (final WorkflowStep step : steps)
            {
                // step = workflowStatus
                String stepEnum = populateString(step.getCode());
                if (stepEnum == null)
                {
                    throw new WorkflowRuntimeException("Cannot find String for enum class: " + step.getCode());
                }
                // populate action bean
                String actionClass = step.getActionClass();
                StepAction action = new StepAction(stepEnum, actionClass);

                if (step.getWaitSecond() != null)
                {
                    action.setTimeoutActionClass(step.getTimeoutActionClass());
                    action.setTimeoutSec(step.getWaitSecond());
                }
                // parent step is any
                String parentStep = populateString(parentStepCode);
                if (StringUtils.isNotBlank(parentStepCode) && parentStep == null)
                {
                    logger.error("Cannot find parent String for enum class: " + parentStepCode);
                    throw new WorkflowRuntimeException("Cannot find parent String for enum class: " + parentStepCode);
                }
                else if (parentStep != null)
                {
                    action.setParentStep(parentStep);

                    List<String> children = subStepsMap.get(parentStep);
                    if (children == null)
                    {
                        children = new ArrayList<>();
                        subStepsMap.put(parentStep, children);
                    }

                    // only add top nodes
                    if (StringUtils.isBlank(step.getTriggerEvents()))
                    {
                        children.add(stepEnum);
                    }

                }

                // set expectedResults
                action.setExpectedResults(parseList(step.getResultEvents()));

                boolean isDup = stepActionMap.put(stepEnum, action) != null;
                if (isDup)
                {
                    throw new WorkflowRuntimeException("Duplicated stepname: " + stepEnum);
                }
                // init progress
                stepProgressMap.put(stepEnum, 0);

            }

            // pass #1 - read steps into memory & cache needed info
            for (final WorkflowStep step : steps)
            {
                String currentStep = populateString(step.getCode());
                StepAction action = stepActionMap.get(currentStep);

                // substeps
                action.setSubSteps(subStepsMap.get(currentStep));

                // check actionClass
                if (action.getSubSteps() == null && StringUtils.isBlank(action.getActionClass()))
                {
                    throw new WorkflowRuntimeException(
                            "Step: " + currentStep + " actionClass undefined for non-nested step");
                }

                // cache trigger->step
                String triggerEvents = step.getTriggerEvents();
                if (StringUtils.isNotBlank(triggerEvents))
                {
                    List<String> triggerEventStrs = parseList(triggerEvents);
                    for (String triggerEvent : triggerEventStrs)
                    {
                        boolean isDup = trigger2StepMap.put(triggerEvent, currentStep) != null;
                        if (isDup)
                        {
                            throw new WorkflowRuntimeException(
                                    currentStep + " has duplicated step triggerEvent: " + triggerEvent);
                        }

                        // check if parent + parentResult exists
                        findTriggerStep(currentStep, triggerEvent);
                    }
                }
                else
                {
                    if (firstStep != null && action.getParentStep() == null)
                    {
                        throw new WorkflowRuntimeException(wfName + " step " + currentStep
                                + " - Only the first step and substeps can have no triggerEvent");
                    }
                    else if (action.getParentStep() == null)
                    {
                        firstStep = currentStep;
                        priority = action.getPriority();
                        poolId = action.getPoolId();
                    }
                }
            }

            // make sure firstStep is there
            if (firstStep == null)
            {
                throw new WorkflowRuntimeException(
                        wfName + " - there must be a starting step without triggerEvent and parentStep");
            }

            // pass #2 populate nextStatus from triggers
            for (final WorkflowStep step : steps)
            {
                String stepEnum = populateString(step.getCode());
                StepAction action = stepActionMap.get(stepEnum);
                Map<Serializable, String> nextStep = new HashMap<>();
                action.setNextStepMap(nextStep);

                // possible outcomes, set next step
                if (StringUtils.isNotBlank(step.getResultEvents()))
                {
                    List<String> results = parseList(step.getResultEvents());
                    // 找 triggerStep+result指定的子流程
                    for (String result : results)
                    {
                        String triggerEvent = WorkflowUtil.computeResultHash(stepEnum, result);
                        if (trigger2StepMap.get(triggerEvent) != null)
                        {
                            nextStep.put(triggerEvent, trigger2StepMap.get(triggerEvent));
                        }
                    }
                }

                // find default
                String stepOnlyTrigger = stepEnum;
                if (trigger2StepMap.get(stepOnlyTrigger) != null)
                {
                    nextStep.put(stepOnlyTrigger, trigger2StepMap.get(stepOnlyTrigger));
                }
            }
        }

        private List<String> parseList(String resultsStr)
        {
            List<String> res = new ArrayList<>();
            if (StringUtils.isBlank(resultsStr))
            {
                return res;
            }
            String[] results = resultsStr.split(",");
            for (String result : results)
            {
                res.add(result.trim());
            }
            return res;
        }

        public WorkflowValidationResult validate(boolean needGraph)
        {
            WorkflowValidationResult result = new WorkflowValidationResult();

            Map<String, Boolean> visitedNodes = new HashMap<>();
            boolean res = validateWorkflow(firstStep, firstStep, stepActionMap, visitedNodes);
            result.setPass(res);
            int visitedSize = visitedNodes.keySet().size();
            if (totalSteps != visitedSize)
            {
                result.setMessage(String.format("Only %d out of %d flows are linked, please check configuration",
                        visitedSize, totalSteps));
            }
            if (needGraph)
            {
                result.setGraph(graphWorkflow());
            }
            return result;
        }

        public String graphWorkflow()
        {
            return graphWorkflow(firstStep);
        }

        public String graphWorkflow(String from)
        {
            WfContentGrapher grapher = new WfContentGrapher(wfName, stepActionMap);
            return grapher.graphWorkflow(from);
        }

        private boolean validateWorkflow(String top, String current, Map<String, StepAction> stepActionMap,
                Map<String, Boolean> visited)
        {

            if (current == null)
            { // end condition
                return true;
            }

            if (visited.get(current) == Boolean.TRUE)
            { // cycle detection
                logger.warn("Cycle detected linking to " + current);
                return false;
            }
            visited.put(current, Boolean.TRUE);
            StepAction currentAction = stepActionMap.get(current);

            // substeps
            List<String> subSteps = currentAction.getSubSteps();
            boolean deadLoop = false;

            if (subSteps != null && subSteps.size() > 0)
            {
                boolean pass = false;
                for (int i = 0; i < subSteps.size(); i++)
                {
                    String subStep = subSteps.get(i);
                    pass = validateWorkflow(top, subStep, stepActionMap, visited) || pass;

                }
                if (!pass)
                {
                    deadLoop = true;
                }
            }

            Map<Serializable, String> nextStepMap = currentAction.getNextStepMap();
            if (nextStepMap.keySet().size() == 0)
            {
                return !deadLoop;
            }

            // next steps
            boolean valid = false;
            for (Serializable result : nextStepMap.keySet())
            {
                String nextStep = nextStepMap.get(result);
                // cycle detection
                valid = validateWorkflow(top, nextStep, stepActionMap, visited) || valid;
            }
            return !deadLoop && valid;
        }

        private String findTriggerStep(String currentStep, String triggerEvent)
        {
            String parentString = null;

            String[] str = triggerEvent.split("-");
            if (str.length > 2)
            {
                throw new WorkflowRuntimeException(
                        "Step=" + currentStep + " has invalid trigger format: " + triggerEvent);
            }

            // find step
            String triggerStep = str[0];
            parentString = triggerStep;
            StepAction parentAction = stepActionMap.get(parentString);
            if (parentString == null)
            {
                throw new WorkflowRuntimeException(
                        "Step=" + currentStep + " has invalid triggerStep enum: " + triggerStep);
            }

            if (parentAction == null)
            {
                throw new WorkflowRuntimeException(
                        "Step=" + currentStep + " has triggerStep [" + triggerStep + "] doesn't exist in config!");
            }

            // 指定了返回值
            if (str.length == 2)
            {
                String triggerResult = str[1];
                List<String> results = parentAction.getExpectedResults();
                boolean found = false;
                for (String result : results)
                {
                    if (result.equals(triggerResult))
                    {
                        found = true;
                        break;
                    }
                }
                if (!found)
                {
                    // results是一个集合类型，按照需要转换成字符串
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; results != null && i < results.size(); i++)
                    {
                        sb.append(results.get(i));
                    }
                    throw new WorkflowRuntimeException(
                            String.format("Step=%s's triggerStep [%s] doesn't have result [%s], expected values: %s",
                                    currentStep, triggerStep, triggerResult, sb.toString()));
                }
            }

            return parentString;
        }
    }

    protected class WfContentGrapher
    {
        // result
        private String graph;

        private String wfName;

        private Map<String, StepAction> stepActionMap;

        public WfContentGrapher(String wfName, Map<String, StepAction> stepActionMap)
        {
            this.wfName = wfName;
            this.stepActionMap = stepActionMap;
        }

        public String graphWorkflow(String firstStep)
        {
            graph = "\n" + wfName + "\n------------------------------------------------\n(S)";
            boolean pass = traverseWorkflow(firstStep, firstStep, stepActionMap, new HashMap<>());
            if (!pass)
                graph += "/deadloop";
            return graph;
        }

        private boolean traverseWorkflow(String top, String current, Map<String, StepAction> stepActionMap,
                Map<String, Boolean> visited)
        {
            if (current == null)
            { // end condition
                graph += ">(E)";
                return true;
            }

            if (visited.get(current) == Boolean.TRUE)
            { // cycle detection
                graph += ">" + current + "/cycle";
                return false;
            }

            StepAction currentAction = stepActionMap.get(current);

            Map<Serializable, String> nextStepMap = currentAction.getNextStepMap();

            // print this step
            graph += ">" + current;
            visited.put(current, Boolean.TRUE);

            List<String> subSteps = currentAction.getSubSteps();
            boolean deadLoop = false;

            if (subSteps != null && subSteps.size() > 0)
            {
                graph += "{";
                int subIndent = findIndentLength(graph, 1);
                graph += "\n" + StringUtils.repeat(" ", subIndent + 1);
                boolean pass = false;
                for (int i = 0; i < subSteps.size(); i++)
                {
                    String subStep = subSteps.get(i);
                    int indent = findIndentLength(graph, 1);
                    pass = traverseWorkflow(top, subStep, stepActionMap, visited) || pass;
                    if (i != subSteps.size() - 1)
                    {
                        graph += "\n" + StringUtils.repeat(" ", indent);
                        // graph += "|";
                    }
                }
                graph += "\n" + StringUtils.repeat(" ", subIndent - 1);
                graph += "}";
                if (!pass)
                {
                    graph += "/deadloop";
                    deadLoop = true;
                }
                graph += "\n" + StringUtils.repeat(" ", subIndent - 1);

            }
            // next steps
            int nextCount = nextStepMap.keySet().size();
            if (nextCount == 0)
            { // end condition
                graph += ">(E)";
                return !deadLoop;
            }
            boolean pass = false;
            for (Serializable result : nextStepMap.keySet())
            {
                String nextStep = nextStepMap.get(result);
                int intent = findIndentLength(graph, 2);
                pass = traverseWorkflow(top, nextStep, stepActionMap, visited) || pass;
                nextCount--;
                if (nextCount != 0)
                {
                    graph += "\n" + StringUtils.repeat(" ", intent + 1);
                    graph += "\\";
                }
            }

            return !deadLoop && pass;
        }

        private int findIndentLength(String graphStr, int backIndent)
        {
            if (graphStr.lastIndexOf("\n") != -1)
            {
                return graphStr.length() - graphStr.lastIndexOf("\n") - backIndent;
            }
            return graphStr.length() - backIndent;
        }

    }

    protected <N> WorkflowData queryProcessData(String processId, WorkflowResponse<N> res)
    {
        long start = System.currentTimeMillis();
        final int timeoutSec = 10;

        InterruptEvent<Void> interruptEvent = WorkflowUtil.buildInterruptEvent(processId, InterruptEnum.QUERY, null);
        String senderCid = engine.createUUID();
        interruptEvent.setSenderCId(senderCid);

        notifyEngineSync(processId, interruptEvent.getCorrelationId(), interruptEvent, "SenderCid=" + senderCid);
        // 从backchannel等待wf发来的消息
        try
        {
            WorkflowData response = (WorkflowData) workflowBackChannel.wait(senderCid, timeoutSec, TimeUnit.SECONDS);
            if (response == null)
            {
                res.addStatus(unknowError("No data returned or Timeout occur timeout=" + timeoutSec + "s"));
                return null;
            }
            logger.info("<<< Responsed after " + (System.currentTimeMillis() - start) + "ms.");
            return response;
        }
        catch (InterruptedException e)
        {
            logger.error(e.getMessage(), e);
            res.addStatus(exception(e.getMessage()));
        }
        return null;
    }

}
