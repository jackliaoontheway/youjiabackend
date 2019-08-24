package com.polarj.initial;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class InitializeAllData
{
    @Test
    @DisplayName("一次性初始化所有数据")
    void initializeAllData()
    {
        initializeI18nResource();
        initializeCountry();
        initializeCurrency();
        initializeFunctionality();
        initializeUserAccountRole();
        initializeUserAccount();
        initializeSerialNumber();
        initializeScheduler();
        initializeModelSpecification();
        initializeFieldSpecification();
        initializeModelOperation();
    }

    @Test
    @DisplayName("初始化国际化资源")
    void initializeI18nResource()
    {
        InitI18nResource initializer = new InitI18nResource();
        String result = initializer.removeExistingDataThenInitialization();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化国家信息")
    void initializeCountry()
    {
        InitCountry initializer = new InitCountry();
        String result = initializer.removeExistingDataThenInitialization();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化货币信息信息（需要先初始化国家信息）")
    void initializeCurrency()
    {
        InitCurrency initializer = new InitCurrency();
        String result = initializer.removeExistingDataThenInitialization();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化预设功能信息")
    void initializeFunctionality()
    {
        InitFunctionality initializer = new InitFunctionality();
        String result = initializer.removeExistingDataThenInitialization();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化用户角色信息（需要先初始化预设功能信息）")
    void initializeUserAccountRole()
    {
        InitUserAccountRole initializer = new InitUserAccountRole();
        String result = initializer.removeExistingDataThenInitialization();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化用户信息（需要先初始化角色信息）")
    void initializeUserAccount()
    {
        InitUserAccount initializer = new InitUserAccount();
        String result = initializer.removeExistingDataThenInitialization();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化序列号生成器信息")
    void initializeSerialNumber()
    {
        InitSerialNumber initializer = new InitSerialNumber();
        String result = initializer.removeExistingDataThenInitialization();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化定时任务信息")
    void initializeScheduler()
    {
        InitScheduler initializer = new InitScheduler();
        List<String> packageNames = new ArrayList<>();
        packageNames.add("com.polarj.common.scheduler");
        initializer.setPackageNames(packageNames);
        String result = initializer.removeExistingDataThenInitialization();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化业务模型描述信息")
    void initializeModelSpecification()
    {
        InitModelSpecification initializer = new InitModelSpecification();
        String result = initializer.addNewDataOnly();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化业务模型属性描述信息")
    void initializeFieldSpecification()
    {
        InitFieldSpecification initializer = new InitFieldSpecification();
        String result = initializer.addNewDataOnly();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }

    @Test
    @DisplayName("初始化业务模型操作功能信息")
    void initializeModelOperation()
    {
        InitModelOperation initializer = new InitModelOperation();
        String result = initializer.addNewDataOnly();
        assertTrue(StringUtils.isEmpty(result), () -> result);
    }
}
