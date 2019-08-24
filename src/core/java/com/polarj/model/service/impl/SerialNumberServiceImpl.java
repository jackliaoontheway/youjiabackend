package com.polarj.model.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.polarj.common.CommonConstant;
import com.polarj.model.SerialNumber;
import com.polarj.model.repository.SerialNumberRepos;
import com.polarj.model.service.SerialNumberService;

@Service
public class SerialNumberServiceImpl extends EntityServiceImpl<SerialNumber, Integer> implements SerialNumberService
{
    private final String datePattern = "${DATE}";

    private final SimpleDateFormat dateFormat = CommonConstant.DateFormatForFile;

    /** 格式 */
    private String pattern = "";

    /** 生成器锁 */
    private final ReentrantLock lock = new ReentrantLock();

    /** 流水号格式化器 */
    private DecimalFormat format = null;

    /** 预生成锁 */
    private final ReentrantLock prepareLock = new ReentrantLock();

    /** 最小值 */
    private int min = 0;

    /** 数字的最长位数 */
    private int digitalCharAmount = 0;

    /** 已生成流水号（种子） */
    private long seed = min;

    /** 预生成数量 */
    private int prepare = 0;

    /** 数据库存储的当前最大序列号 **/
    private long maxSerialInt = 0;

    // 当前缓存中的日期字串值
    private String dateInPattern = null;

    /** 预生成流水号 */
    private HashMap<String, List<String>> prepareSerialNumberMap = new HashMap<>();

    // QUES：带有日期的模式，如何在转变日期时重新开始序列号（已经解决）
    // 如何验证锁机制
    // 序列号丢失的问题（缓冲池中的序列号，没有使用完，一旦系统出现问题，就丢失掉所有没有使用的序列号）
    public String generateSerialNumberByModelCode(String moduleCode)
    {
        // 预序列号加锁
        prepareLock.lock();
        try
        {
            // 判断内存中是否还有序列号
            if (null != prepareSerialNumberMap.get(moduleCode) && prepareSerialNumberMap.get(moduleCode).size() > 0)
            {
                String currentDate = dateFormat.format(new Date());
                if (!pattern.contains(datePattern) || currentDate.equals(dateInPattern))
                {
                    // 若有且是同一天，返回第一个，并删除
                    return prepareSerialNumberMap.get(moduleCode).remove(0);
                }
            }
        }
        finally
        {
            // 预序列号解锁
            prepareLock.unlock();
        }
        SerialNumber sn = fetchSNForModule(moduleCode);
        prepare = sn.getInCacheSnAmount();// 预生成流水号数量
        pattern = sn.getSnPattern().trim();// 配置模板
        dateInPattern = sn.getLastDateInSn();
        String currentDate = dateFormat.format(new Date());
        if (pattern.contains(datePattern) && !currentDate.equals(dateInPattern))
        {// 如果有日期在序列号中，同时与上一次生成的日期不一致时，需要日期序列号重新开始
            sn.setCurrentSn(0l);
            update(sn.getId(), sn, CommonConstant.systemUserAccountId, CommonConstant.defaultSystemLanguage);
        }
        maxSerialInt = sn.getCurrentSn(); // 存储当前最大值
        digitalCharAmount = counter(pattern, '0'); // 算一下一共有多少位数的数字
        format = new DecimalFormat(pattern);
        // 生成预序列号，存到缓存中
        List<String> resultList = generatePrepareSerialNumbers(moduleCode, sn);
        prepareLock.lock();
        try
        {
            prepareSerialNumberMap.put(moduleCode, resultList);
            return prepareSerialNumberMap.get(moduleCode).remove(0);
        }
        finally
        {
            prepareLock.unlock();
        }
    }

    @Override
    public SerialNumber fetchSNForModule(String moduleCode)
    {
        SerialNumberRepos snRepos = (SerialNumberRepos) getRepos();
        SerialNumber sn = snRepos.findByModuleCode(moduleCode);
        return sn.deepClone(1, logger);
    }

    private List<String> generatePrepareSerialNumbers(String moduleCode, SerialNumber sn)
    {
        // 临时List变量
        List<String> resultList = new ArrayList<String>(prepare);
        lock.lock();
        try
        {
            dateInPattern = dateFormat.format(new Date());
            for (int i = 0; i < prepare; i++)
            {
                maxSerialInt = maxSerialInt + 1;
                if (maxSerialInt > min && (maxSerialInt + "").length() < digitalCharAmount)
                {
                    seed = maxSerialInt;
                }
                else
                {
                    // 如果动态数字长度大于模板中的长度 例：模板CF000 maxSerialInt 1000
                    seed = maxSerialInt = 0;
                    sn.setCurrentSn(0l);
                    update(sn.getId(), sn, CommonConstant.systemUserAccountId, CommonConstant.defaultSystemLanguage);
                }
                // 动态数字生成
                String formatSerialNum = format.format(seed);

                // 动态日期的生成
                if (pattern.contains(datePattern))
                {
                    formatSerialNum = formatSerialNum.replace(datePattern, dateInPattern);
                }
                resultList.add(formatSerialNum);
            }
            // 更新数据
            sn.setCurrentSn(maxSerialInt);
            sn.setLastDateInSn(dateInPattern);
            update(sn.getId(), sn, CommonConstant.systemUserAccountId, CommonConstant.defaultSystemLanguage);
        }
        finally
        {
            lock.unlock();
        }
        return resultList;
    }

    private int counter(String str, char c)
    {
        int count = 1;
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == c)
            {
                count++;
            }
        }
        return count;
    }
}
