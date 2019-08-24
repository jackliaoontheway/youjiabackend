package com.polarj.workflow;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// QUES（已经增加一个保存等待操作的列表）： 如何确定一个动作是不是工作流等待的动作呢？
// 是不是应该有一个双队列，一个是工作流需要的操作，
// 一个是根据工作流需要，从人工操作而来的动作信息？
// QUES: 同一个模型实例可能处在不同的工作流实例下。
// 有没有必要让一个模型可以在多个不同工作流管理下

public class WebpageOperationQueue
{
    protected final Logger logger = LoggerFactory.getLogger(WebpageOperationQueue.class);

    // 页面的操作用如下格式保存在map中
    // Key: 模型类名-模型的实例ID
    // Value（operation, operId, languageId）: 所做的操作，操作者，操作者的界面语言
    private ConcurrentMap<String, String> webpageOperations = new ConcurrentHashMap<>(100);

    // 工作流在等待的操作
    // Key: 模型类名-模型的实例ID
    // Value: 等待的所有操作
    private ConcurrentMap<String, List<String>> expectedOperations = new ConcurrentHashMap<String, List<String>>(100);

    // 当保存网页操作没有出错，返回真， 否则返回假。
    // 返回真有两种可能，是工作流等待的操作，且保存好了，或者不是工作流等待的操作，可以不用在意
    public boolean addOperation(String modelClassName, Integer modelId, String webpageOperation, Integer operId,
            String languageId)
    {
        String modelInfo = modelClassName + "-" + modelId;
        List<String> expectedOps = expectedOperations.get(modelInfo);
        if (expectedOps == null || expectedOps.size() == 0 || !expectedOps.contains(webpageOperation))
        {
            // 这个不是工作流等待的操作，外部流程可以不用在意。
            return true;
        }
        // 等待的操作已经到了。把等待的信息删除
        expectedOperations.remove(modelInfo);
        String oldValue = null;
        boolean hasError = false;
        try
        {
            oldValue = webpageOperations.putIfAbsent(modelClassName + "-" + modelId, webpageOperation);
            // 如果oldValue为空， 这是一个工作流等待的操作，等待工作流处理，没有错误。
            // 如果oldValue不为空，意味着有错误。
            if (oldValue != null)
            {
                hasError = true;
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            hasError = true;
        }
        return !hasError;
    }

    public void addExpectedOperations(String modelClassName, Integer modelId, List<String> operations)
    {
        String modelInfo = modelClassName + "-" + modelId;
        expectedOperations.putIfAbsent(modelInfo, operations);
    }

    public String getManullyOperationFor(String modelClassName, Integer modelId)
    {
        String modelInfo = modelClassName + "-" + modelId;
        String operationForModel = webpageOperations.get(modelInfo);
        if (operationForModel != null)
        {
            webpageOperations.remove(modelInfo);
        }
        return operationForModel;
    }

}
