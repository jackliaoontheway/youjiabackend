package com.polarj.common.utility;

import java.io.Serializable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.service.EntityService;

@Component
public class SpringContextUtils implements ApplicationContextAware
{

    private static ApplicationContext applicationContext = null;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public static Object getBean(Class<?> clazz) throws BeansException, ClassNotFoundException
    {
        return applicationContext.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <M extends GenericDbInfo, ID extends Serializable> EntityService<M, ID> getModelServiceBean(
            Class<M> modelClazz)
    {
        String servicePackageName = modelClazz.getPackage().getName() + ".service";
        String serviceInterfaceName = modelClazz.getSimpleName() + "Service";
        return (EntityService<M, ID>) getBean(servicePackageName + "." + serviceInterfaceName);
    }

    public static Object getBean(String clazzName)
    {
        Class<?> clazz = null;
        Object o = null;
        try
        {
            clazz = Class.forName(clazzName);
            o = applicationContext.getBean(clazz);
        }
        catch (Exception e)
        {
            o = null;
            e.printStackTrace();
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBeanByName(String name) throws BeansException
    {
        return (T) applicationContext.getBean(name);
    }

    public static Object getBean(String name, Class<?> requiredType) throws BeansException
    {
        return applicationContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name)
    {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException
    {
        return applicationContext.isSingleton(name);
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException
    {
        return applicationContext.getType(name);
    }

    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException
    {
        return applicationContext.getAliases(name);
    }
}
