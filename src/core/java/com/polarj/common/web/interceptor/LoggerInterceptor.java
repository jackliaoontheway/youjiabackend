package com.polarj.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.Setter;

public final class LoggerInterceptor extends HandlerInterceptorAdapter
{
    private @Setter boolean needLogger = false;

    private @Setter String resourcesLink = "";

    protected final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (needLogger)
        {
            String servletPath = request.getServletPath();
            String accessIP = request.getRemoteAddr();
            if (!servletPath.contains(resourcesLink))
            {
                logger.info("'" + servletPath + "' is accessed from " + accessIP);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        if (needLogger)
        {
            String servletPath = request.getServletPath();
            if (!servletPath.contains(resourcesLink))
            {
                logger.info("access '" + servletPath + "' is finished.");
            }
        }
    }
}
