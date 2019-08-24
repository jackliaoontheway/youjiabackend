package com.polarj.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "shiro")
public class PolarjShiroFilterConfig
{
    private @Getter @Setter String[] anonUrls;

    private @Getter @Setter String unauthorizedUrl;
}
