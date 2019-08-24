package com.polarj.model.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.polarj.PolarjHibernateConfig;
import com.polarj.common.CommonConstant;
import com.polarj.common.utility.FieldValueUtil;
import com.polarj.model.HasSubWithFieldInJson;
import com.polarj.model.ModelInheritanceRel;
import com.polarj.model.ModelSpecification;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.annotation.ModelMetaDataLabel;
import com.polarj.model.repository.ModelSpecificationRepos;
import com.polarj.model.service.ModelInheritanceRelService;
import com.polarj.model.service.ModelSpecificationService;

@Service
public class ModelSpecificationServiceImpl extends CachedEntityServiceImpl<ModelSpecification>
        implements ModelSpecificationService
{
    @Resource
    private PolarjHibernateConfig config;

    @Autowired
    @Lazy
    private ModelInheritanceRelService modelRelService;

    @Override
    public ModelSpecification fetchModelSpecificationByClassName(String className, String languageId)
    {
        ModelSpecificationRepos repos = (ModelSpecificationRepos) getRepos();
        ModelSpecification mSpec = repos.findOneByClassName(className);
        return replaceI18nFieldValueWithResource(dataClone(mSpec), languageId);
    }

    @Override
    public ModelSpecification generateModelSpecificationByModelMetaData(Class<?> modelClazz, String languageId)
    {
        if (modelClazz == null)
        {
            logger.error("Can not find modelClazz in the system.");
            return null;
        }
        String className = modelClazz.getName();
        ModelMetaData mmd = findModelMetaData(modelClazz, languageId);
        if (mmd == null)
        {
            logger.warn("ModelMetaData is not presented for {} and {} does not have super class "
                    + "implements HasSubWithFieldInJson.", className, className);
            return null;
        }
        ModelSpecification mSpec = ModelSpecification.generateFromClassWithModelMetaDataAnnotation(className, mmd);
        return mSpec;
    }

    // 找到这个类或者是这个类被HasSubWithFieldInJson注解的父类的ModelMetaData
    private ModelMetaData findModelMetaData(Class<?> modelClazz, String languageId)
    {
        ModelMetaData mmd = modelClazz.getAnnotation(ModelMetaData.class);
        if (mmd != null)
        {
            if (HasSubWithFieldInJson.class.isAssignableFrom(modelClazz)
                    && !Modifier.isAbstract(modelClazz.getModifiers()))
            {
                saveModelInheritanceRel(modelClazz, modelClazz, mmd.label(), languageId);
            }
            return mmd;
        }
        Class<?> parentClazz = modelClazz.getSuperclass();
        Class<?> subClaszz = modelClazz;
        while (mmd == null && !parentClazz.equals(Object.class))
        {
            mmd = parentClazz.getAnnotation(ModelMetaData.class);
            if (mmd != null)
            {
                if (HasSubWithFieldInJson.class.isAssignableFrom(parentClazz)
                        && !Modifier.isAbstract(parentClazz.getModifiers()))
                {
                    ModelMetaDataLabel mmdl = modelClazz.getAnnotation(ModelMetaDataLabel.class);
                    String label = mmd.label();
                    if (mmdl != null)
                    {
                        label = mmdl.label();
                    }
                    saveModelInheritanceRel(parentClazz, subClaszz, label, languageId);
                    return mmd;
                }
            }
            subClaszz = parentClazz;
            parentClazz = parentClazz.getSuperclass();
        }
        return null;
    }

    private void saveModelInheritanceRel(Class<?> baseClazz, Class<?> subClazz, String label, String languageId)
    {
        ModelInheritanceRel modelRel = new ModelInheritanceRel();
        modelRel.setBaseClassName(baseClazz.getName());
        modelRel.setSubClassName(subClazz.getName());
        modelRel.setLabel(label);
        if (modelRelService.fetchByUniqueData(baseClazz.getName(), subClazz.getName()) == null)
        {
            modelRelService.create(modelRel, CommonConstant.systemUserAccountId, languageId);
        }
    }

    @Override
    public List<ModelSpecification> fetchAllSubModelSpecificationByClassName(String className, String languageId)
    {
        List<ModelSpecification> mSpecs = new ArrayList<ModelSpecification>();
        ModelSpecificationRepos repos = (ModelSpecificationRepos) getRepos();
        try
        {
            // 获取了业务模型的类型
            Class<?> modelClazz = Class.forName(className);
            if (modelClazz == null)
            {
                logger.error("Can not find {} in the system.", className);
                return mSpecs;
            }
            List<String> clazzNames = new ArrayList<>();
            if (HasSubWithFieldInJson.class.isAssignableFrom(modelClazz))
            {
                // 如果业务模型是HasSubWithFieldInJson的实现类，
                // 需要通过ModelInheritanceRel类得到所有的子类的名称（包括自己）
                List<ModelInheritanceRel> modelRels = modelRelService
                        .fetchModelInheritanceRelByBaseClassName(className);
                if (CollectionUtils.isNotEmpty(modelRels))
                {
                    for (ModelInheritanceRel modelRel : modelRels)
                    {
                        if (StringUtils.isNotEmpty(modelRel.getSubClassName()))
                        {
                            clazzNames.add(modelRel.getSubClassName());
                        }
                    }
                }
            }
            if (CollectionUtils.isEmpty(clazzNames))
            {
                clazzNames.add(className);
            }
            for (String clazzName : clazzNames)
            {
                ModelSpecification mSpec = repos.findOneByClassName(clazzName);
                if (mSpec == null)
                {
                    mSpec = generateModelSpecificationByModelMetaData(Class.forName(clazzName), languageId);
                }
                mSpecs.add(replaceI18nFieldValueWithResource(mSpec, languageId));
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return replaceI18nFieldValueWithResource(dataClone(mSpecs), languageId);
    }

    @Override
    protected void setUniqueField()
    {
        Field[] uniqueFields = new Field[1];
        uniqueFields[0] = FieldValueUtil.getTheField(ModelSpecification.class, "className", logger);
        if (uniqueFields[0] != null)
        {
            setUniqueFields(uniqueFields);
        }
    }
}
