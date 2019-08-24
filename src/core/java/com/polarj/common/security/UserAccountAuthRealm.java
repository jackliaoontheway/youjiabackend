package com.polarj.common.security;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.polarj.model.Functionality;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;
import com.polarj.model.enumeration.UserAccountStatus;
import com.polarj.model.service.UserAccountService;

import lombok.Setter;

@Component("userRealm")
public class UserAccountAuthRealm extends AuthorizingRealm
{
    @Autowired
    @Lazy
    private @Setter UserAccountService userAccountServiceImpl;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        UserAccount userAcc = (UserAccount) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (null == userAcc)
        {
            return null;
        }

        List<UserAccountRole> userAccRoles = userAcc.getAllRoles();
        if (userAccRoles == null)
        {
            return info;
        }

        for (UserAccountRole role : userAccRoles)
        {
            info.addRole(role.getCode());
            List<Functionality> functions = role.getFunctions();
            if (functions != null && functions.size() > 0)
            {
                for (Functionality functionality : functions)
                {
                    if (functionality != null && StringUtils.isNotEmpty(functionality.getCode()))
                    {
                        info.addStringPermission(functionality.getCode());
                    }
                }
            }
        }

        return info;
    }
    

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException
    {
        UserAccountToken token = (UserAccountToken) authToken;
        UserAccount userAcc = userAccountServiceImpl.fetchByName(token.getUsername());
        if (null == userAcc || !UserAccountStatus.ACTIVE.name().equalsIgnoreCase(userAcc.getStatus()))
        {
            throw new AccountException("the username/password incorrect.");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userAcc, userAcc.getLoginName(), getName());
        return info;
    }

}
