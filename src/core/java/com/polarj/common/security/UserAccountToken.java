package com.polarj.common.security;

import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.Getter;
import lombok.Setter;

public class UserAccountToken extends UsernamePasswordToken
{
    private static final long serialVersionUID = 6081767266786174616L;

    private @Getter @Setter String stringPassword;

    public UserAccountToken(String userName, String password)
    {
        super(userName, password);
        this.stringPassword = password;
    }
}
