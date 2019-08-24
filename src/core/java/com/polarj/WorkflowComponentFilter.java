package com.polarj;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

public class WorkflowComponentFilter implements TypeFilter
{

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException
    {
        YamlPropertiesFactoryBean ypfb = new YamlPropertiesFactoryBean();
        ypfb.setResources(new ClassPathResource("application.yml"));
        Properties properties = ypfb.getObject();
        boolean includeWorkflow = Boolean.parseBoolean(properties.getProperty("workflow.enable"));
        if (!includeWorkflow && metadataReader.getClassMetadata().getClassName().startsWith("com.polarj.workflow"))
        {
            return true;
        }
        return false;
    }

}
