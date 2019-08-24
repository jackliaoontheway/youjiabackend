package com.polarj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { "com" }, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = { WorkflowComponentFilter.class }) })
public class PolarjApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(PolarjApplication.class);
    }

    public static void main(String[] args)
    {
        SpringApplication app = new SpringApplication(PolarjApplication.class);
        app.run(args);
    }
}
