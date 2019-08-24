package com.polarj.common.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import com.polarj.model.UserAccount;

public class UserAccountCredentialMatcher implements CredentialsMatcher
{
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
    {
        UserAccountToken userAccToken = (UserAccountToken) token;
        UserAccount userAcc = (UserAccount) info.getPrincipals().getPrimaryPrincipal();
        if (userAcc == null)
        {
            return false;
        }
        boolean res = userAcc.isSameUser(userAccToken.getUsername(), userAccToken.getStringPassword());
        return res;
    }
}
