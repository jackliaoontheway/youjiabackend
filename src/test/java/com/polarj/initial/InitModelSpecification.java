package com.polarj.initial;

import java.util.ArrayList;
import java.util.List;

import com.polarj.PolarjHibernateConfig;
import com.polarj.common.utility.ReflectionUtil;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.ModelSpecification;
import com.polarj.model.service.ModelSpecificationService;

public class InitModelSpecification extends AbstractInitializeData<ModelSpecification, ModelSpecificationService>
{
    private PolarjHibernateConfig config;

    public InitModelSpecification()
    {
        super(ModelSpecification.class, ModelSpecificationService.class);
        try
        {
            config = (PolarjHibernateConfig) SpringContextUtils.getBean(PolarjHibernateConfig.class);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    protected String initialData(boolean removeExistingData)
    {
        if (config == null)
        {
            return "No config injected for: PolarjHibernateConfig.";
        }
        return super.initialData(removeExistingData);
    }

    @Override
    protected List<ModelSpecification> fetchDataFromDataSource()
    {
        List<ModelSpecification> mSpecs = new ArrayList<>();
        for (String packageName : config.getPackagesToScan())
        {
            List<Class<?>> classes = ReflectionUtil.listClassWithAnnotationUnderPackage(packageName, null);
            if (classes == null || classes.size() == 0)
            {
                logger.info("No model found under: " + packageName);
                continue;
            }
            for (Class<?> clazz : classes)
            {
                if (clazz.isInterface())
                {
                    continue;
                }
                logger.info("Will initialize {}'s ModelSpecification.", clazz.getSimpleName());
                ModelSpecification mSpec = getService().generateModelSpecificationByModelMetaData(clazz,
                        languageUsedInDataSource());
                if (mSpec != null)
                {
                    mSpecs.add(mSpec);
                }
            }
        }
        return mSpecs;
    }

    @Override
    protected boolean isExisting(ModelSpecification entity)
    {
        if (entity == null)
        {
            return false;
        }
        ModelSpecification mSpec = getService().fetchModelSpecificationByClassName(entity.getClassName(),
                languageUsedInDataSource());
        return mSpec == null ? false : true;
    }

    @Override
    protected String languageUsedInDataSource()
    {
        return languageId;
    }
}
