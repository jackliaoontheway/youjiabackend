package com.polarj.common.utility;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReflectionUtil
{
    private static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    public static List<Class<?>> listClassWithAnnotationUnderPackage(String packageName,
            Class<? extends Annotation> annotationClass)
    {
        String relPath = packageName.replace('.', '/');
        logger.debug("List class for package: " + packageName);
        Enumeration<URL> resources = null;
        try
        {
            resources = ClassLoader.getSystemResources(relPath);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resources = null;
        }
        if (resources == null || !resources.hasMoreElements())
        {
            logger.warn("No resource for " + relPath);
            return null;
        }
        List<Class<?>> classes = new ArrayList<Class<?>>();

        while (resources.hasMoreElements())
        {
            List<Class<?>> res =
                    listClassWithAnnotationUnderResource(packageName, resources.nextElement(), annotationClass);
            if (res != null && res.size() > 0)
            {
                classes.addAll(res);
            }
        }
        return classes;
    }

    private static List<Class<?>> fetchModelClassesFromDirectory(String packageName, String fullPath,
            Class<? extends Annotation> annotationClass)
    {
        File directory = null;
        List<Class<?>> classes = new ArrayList<Class<?>>();
        try
        {
            directory = new File(fullPath);
        }
        catch (IllegalArgumentException e)
        {
            logger.error(e.getMessage(), e);
            directory = null;
        }
        if (directory == null)
        {
            logger.info("No directory found for package: " + packageName);
            return null;
        }
        logger.debug("Directory = " + directory);
        String[] files = directory.list();
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].endsWith(".class"))
            {
                String className = packageName + '.' + files[i].substring(0, files[i].length() - 6);
                logger.debug("ClassDiscovery: className = " + className);
                try
                {
                    Class<?> clazz = Class.forName(className);
                    if (clazz != null)
                    {
                        if (annotationClass == null || clazz.isAnnotationPresent(annotationClass))
                        {
                            classes.add(clazz);
                        }
                    }
                }
                catch (ClassNotFoundException e)
                {
                    logger.error("ClassNotFoundException for loading " + className);
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return classes;
    }

    private static List<Class<?>> fetchModelClassesFromJar(String packageName, String fullPath,
            Class<? extends Annotation> annotationClass)
    {
        JarFile jarFile = null;
        String relPath = packageName.replace('.', '/');
        List<Class<?>> classes = new ArrayList<Class<?>>();
        try
        {
            String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements())
            {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(relPath) && entryName.endsWith(".class"))
                {
                    logger.debug("ClassDiscovery: JarEntry: " + entryName);
                    String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                    logger.debug("ClassDiscovery: className = " + className);
                    try
                    {
                        Class<?> clazz = Class.forName(className);
                        if (clazz != null)
                        {
                            if (annotationClass == null || clazz.isAnnotationPresent(annotationClass))
                            {
                                classes.add(clazz);
                            }
                        }
                    }
                    catch (ClassNotFoundException e)
                    {
                        logger.error("ClassNotFoundException for loading " + className);
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        catch (IOException e)
        {
            logger.error(packageName + " does not appear to be a valid package", e);
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (jarFile != null)
            {
                try
                {
                    jarFile.close();
                }
                catch (IOException e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return classes;
    }

    private static List<Class<?>> listClassWithAnnotationUnderResource(String packageName, URL resource,
            Class<? extends Annotation> annotationClass)
    {
        String fullPath = resource.getFile();
        logger.debug("Will find class under path: " + resource);

        List<Class<?>> classes = null;
        if (resource.getProtocol().trim().equalsIgnoreCase("jar"))
        {
            classes = fetchModelClassesFromJar(packageName, fullPath, annotationClass);
        }
        else
        {
            classes = fetchModelClassesFromDirectory(packageName, fullPath, annotationClass);
        }
        return classes;
    }
}
