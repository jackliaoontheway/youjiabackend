package com.polarj.initial;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.polarj.PolarjHibernateConfig;
import com.polarj.common.utility.ReflectionUtil;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.FieldSpecification;
import com.polarj.model.service.FieldSpecificationService;

public class InitFieldSpecification extends AbstractInitializeData<FieldSpecification, FieldSpecificationService>
{
    private PolarjHibernateConfig config;

    public InitFieldSpecification()
    {
        super(FieldSpecification.class, FieldSpecificationService.class);
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
    protected List<FieldSpecification> fetchDataFromDataSource()
    {
        List<FieldSpecification> fSpecs = new ArrayList<>();
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
                logger.info("Will initialize {}'s FieldSpecification.", clazz.getSimpleName());
                List<FieldSpecification> modelFieldSpecs = getService().generateFieldSpecificationByFieldMetaData(clazz,
                        languageUsedInDataSource());
                if (modelFieldSpecs == null)
                {
                    continue;
                }
                for(FieldSpecification fSpec: modelFieldSpecs)
                {
                    if(!contains(fSpecs, fSpec))
                    {
                        fSpecs.add(fSpec);
                    }
                }
            }
        }
        return fSpecs;
    }

    private boolean contains(List<FieldSpecification> fSpecs, FieldSpecification fSpec)
    {
        for(FieldSpecification fs: fSpecs)
        {
            if(fs.getFieldFullName().equals(fSpec.getFieldFullName()))
            {
                return true;
            }
        }
        return false;
    }
    @Override
    protected String languageUsedInDataSource()
    {
        return languageId;
    }

    @Override
    protected boolean isExisting(FieldSpecification entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getFieldFullName()))
        {
            return false;
        }
        FieldSpecification m = getService().fetchByUniqueData(entity.getFieldFullName(), languageUsedInDataSource());
        return m == null ? false : true;
    }

}
