package com.polarj;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "hibernate")
public class PolarjHibernateConfig
{
    private @Getter @Setter String ddlAuto;

    private @Getter @Setter String dialect;

    private @Getter @Setter Boolean showSql;

    private @Getter @Setter Boolean generateDdl;

    private @Getter @Setter String[] packagesToScan;
}
