package com.polarj.common.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class OptionsMethodShiroFilter extends FormAuthenticationFilter
{
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
    {
        if (request instanceof HttpServletRequest)
        {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS"))
            {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
