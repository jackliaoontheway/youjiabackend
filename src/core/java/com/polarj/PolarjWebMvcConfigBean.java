package com.polarj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class PolarjWebMvcConfigBean implements WebMvcConfigurer
{
    @Autowired
    private PolarjCorsConfig corsCfg;

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**").allowCredentials(corsCfg.getSupportCredentials())
                .allowedOrigins(corsCfg.getAllowedOrigins()).allowedHeaders(corsCfg.getAllowedHeaders())
                .allowedMethods(corsCfg.getAllowedMethods()).exposedHeaders(corsCfg.getExposedHeaders())
                .maxAge(corsCfg.getPreflightMaxage());
    }
}
