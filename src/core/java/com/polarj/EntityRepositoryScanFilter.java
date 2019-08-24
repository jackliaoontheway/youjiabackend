package com.polarj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

public class EntityRepositoryScanFilter implements TypeFilter
{
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException
    {
        YamlPropertiesFactoryBean ypfb = new YamlPropertiesFactoryBean();
        ypfb.setResources(new ClassPathResource("application.yml"));
        Properties properties = ypfb.getObject();
        List<String> repositoriesToScan = new ArrayList<>();
        for (int i = 0;; i++)
        {
            String repositoryPackage = properties.getProperty("hibernate.repositoriesToScan[" + i + "]");
            if (StringUtils.isEmpty(repositoryPackage))
            {
                break;
            }
            repositoriesToScan.add(repositoryPackage);
        }
        for (String basePackage : repositoriesToScan)
        {
            if (metadataReader.getClassMetadata().getClassName().startsWith(basePackage))
            {
                return true;
            }
        }
        return false;
    }
}
