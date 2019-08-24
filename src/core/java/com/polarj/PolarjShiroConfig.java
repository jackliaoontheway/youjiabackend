package com.polarj;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.polarj.common.security.OptionsMethodShiroFilter;
import com.polarj.common.security.PolarjShiroFilterConfig;
import com.polarj.common.security.UserAccountAuthRealm;
import com.polarj.common.security.UserAccountCredentialMatcher;

@Component
public class PolarjShiroConfig
{
    @Bean("shiroSecurityManager")
    @DependsOn(value = "authRealm")
    public org.apache.shiro.mgt.SessionsSecurityManager securityManager(@Qualifier("authRealm") Realm authRealm,
            @Qualifier("credentialMatcher") CredentialsMatcher matcher)
    {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }

    @Bean("authRealm")
    public Realm realm(@Qualifier("userRealm") Realm authRealm,
            @Qualifier("credentialMatcher") CredentialsMatcher matcher)
    {
        ((UserAccountAuthRealm) authRealm).setCredentialsMatcher(matcher);
        return authRealm;
    }

    @Bean
    public EventBus eventBus()
    {
        EventBus eventBus = new DefaultEventBus();
        return eventBus;
    }

    @Bean("credentialMatcher")
    public CredentialsMatcher credentialsMatcher()
    {
        CredentialsMatcher matcher = new UserAccountCredentialMatcher();
        return matcher;
    }

    @Bean
    @DependsOn(value = "shiroSecurityManager")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(PolarjShiroFilterConfig shiroFilterConfig,
            @Qualifier("shiroSecurityManager") org.apache.shiro.mgt.SessionsSecurityManager securityManager)
    {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, String> filterRuleMap = new HashMap<>();
        String[] anonUrls = shiroFilterConfig.getAnonUrls();
        if (anonUrls != null && anonUrls.length > 0)
        {
            for (String anonUrl : anonUrls)
            {
                filterRuleMap.put(anonUrl, "anon");
            }
        }
        else
        {
            filterRuleMap.put("/loginaudit/login", "anon");
            filterRuleMap.put("/languages", "anon");
        }
        filterRuleMap.put("/", "anon");
        filterRuleMap.put("/**", "authc");
        factoryBean.getFilters().put("authc", new OptionsMethodShiroFilter());
        factoryBean.setLoginUrl("/loginaudit/login");
        factoryBean.setUnauthorizedUrl(shiroFilterConfig.getUnauthorizedUrl());
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    @Bean
    @DependsOn(value = "shiroSecurityManager")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("shiroSecurityManager") org.apache.shiro.mgt.SessionsSecurityManager securityManager)
    {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator()
    {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor()
    {
        return new LifecycleBeanPostProcessor();
    }
}
