package com.polarj;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "cors")
public class PolarjCorsConfig
{
    private @Getter @Setter String[] allowedOrigins;

    private @Getter @Setter String[] allowedMethods;

    private @Getter @Setter String[] allowedHeaders;

    private @Getter @Setter String[] exposedHeaders;

    private @Getter @Setter Boolean supportCredentials;
    
    private @Getter @Setter Long preflightMaxage;
}
