package com.polarj.model.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.stereotype.Service;

import com.polarj.PolarjHibernateConfig;
import com.polarj.common.utility.FieldValueUtil;
import com.polarj.common.utility.ReflectionUtil;
import com.polarj.model.FieldSpecification;
import com.polarj.model.UserAccount;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.repository.FieldSpecificationRepos;
import com.polarj.model.service.FieldSpecificationService;

@Service
public class FieldSpecificationServiceImpl extends CachedEntityServiceImpl<FieldSpecification>
        implements FieldSpecificationService
{
    @Resource
    private PolarjHibernateConfig config;

    @Override
    protected void setUniqueField()
    {
        Field[] uniqueFields = new Field[1];
        uniqueFields[0] = FieldValueUtil.getTheField(FieldSpecification.class, "fieldFullName", logger);
        if (uniqueFields[0] != null)
        {
            setUniqueFields(uniqueFields);
        }
    }

    @Override
    public List<FieldSpecification> fetchByModelName(String modelName, String languageId)
    {
        List<FieldSpecification> res = fetchFromCacheByUniqueValuePattern(modelName + ".", languageId);
        return res;
    }

    @Override
    public void refreshFieldSpecificationForModelPackages(UserAccount userAcc, String languageId)
    {
        String[] modelPackages = config.getPackagesToScan();
        for (String mp : modelPackages)
        {
            refreshFieldSpecificationForModelPackage(mp.trim(), userAcc, languageId);
        }
    }

    private void refreshFieldSpecificationForModelPackage(String packageName, UserAccount userAcc, String languageId)
    {
        List<Class<?>> classes = ReflectionUtil.listClassWithAnnotationUnderPackage(packageName, Entity.class);
        if (classes == null || classes.size() == 0)
        {
            logger.info("No model found under: " + packageName);
            return;
        }
        for (Class<?> clazz : classes)
        {
            logger.debug("Will generate FieldMetaData for: {}.", clazz.getName());
            getFieldMetaData(clazz, userAcc, languageId);
        }
    }

    @Override
    public FieldSpecification fetchByUniqueData(String fieldFullName, String languageId)
    {
        FieldSpecificationRepos repos = (FieldSpecificationRepos) getRepos();
        FieldSpecification fSpec = repos.findByFieldFullName(fieldFullName);
        return this.replaceI18nFieldValueWithResource(dataClone(fSpec), languageId);
    }

    @Override
    public List<FieldSpecification> generateFieldSpecificationByFieldMetaData(Class<?> modelClazz, String languageId)
    {
        Field[] modelFields = FieldValueUtil.getAllFields(modelClazz, logger);
        if (modelFields == null || modelFields.length == 0)
        {
            return null;
        }
        String classFullName = modelClazz.getName();
        List<FieldSpecification> fSpecs = new ArrayList<>();
        for (Field modelField : modelFields)
        {
            FieldMetaData fMeta = modelField.getAnnotation(FieldMetaData.class);
            if (fMeta == null)
            {
                // 忽略没有被FieldMetaData注解的属性
                continue;
            }
            FieldSpecification fSpec = FieldSpecification.generateFieldSpecificationByFieldMetaData(fMeta,
                    classFullName, modelField.getName());
            if (fSpec == null)
            {
                continue;
            }
            Column c = modelField.getAnnotation(Column.class);
            if (c != null && modelField.getType().equals(String.class))
            {
                if (fSpec.getMaxLength() == 0 || fSpec.getMaxLength() > c.length())
                {
                    fSpec.setMaxLength(c.length());
                }
            }
            fSpecs.add(fSpec);
        }
        return fSpecs;
    }
}
