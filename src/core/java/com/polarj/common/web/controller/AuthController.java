package com.polarj.common.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polarj.common.CommonConstant;
import com.polarj.common.security.UserAccountToken;
import com.polarj.common.web.model.ClientRequest;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.model.LoginAudit;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;
import com.polarj.model.service.LoginAuditService;
import com.polarj.model.service.UserAccountService;

@Controller
@RequestMapping("/loginaudit")
public class AuthController extends BaseController
{
    @Autowired
    private LoginAuditService loginAuditService;

    @Autowired
    private UserAccountService uaService;
    
    @PostMapping(value = "login")
    public ResponseEntity<ServerResponse<UserAccount>> addLoginAudit(
            @RequestBody ClientRequest<UserAccount> clientRequest, HttpServletRequest request,
            HttpServletResponse response)
    {
        UserAccount loginUserAcc = clientRequest.getData();
        UserAccountToken token = new UserAccountToken(loginUserAcc.getLoginName(), loginUserAcc.getPassword());
        Subject currentUser = SecurityUtils.getSubject();
        try
        {
            currentUser.login(token);
        }
        catch (AccountException e)
        {
            logger.error(e.getMessage(), e);
            return generateHttpErrorRESTFulResponse(HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateHttpErrorRESTFulResponse(HttpStatus.UNAUTHORIZED);
        }

        if (!currentUser.isAuthenticated())
        {
            return generateHttpErrorRESTFulResponse(HttpStatus.UNAUTHORIZED);
        }
        saveLoginAudit(loginUserAcc.getLoginName(), loginUserAcc.getPasswordHash(), request, response);
        return currentLogin();
    }

    @GetMapping(value = "login")
    public ResponseEntity<ServerResponse<UserAccount>> currentLogin()
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null || userAcc.getUserSetting() == null || userAcc.getAllRoles() == null)
        {
            // 没有登录用户信息，或者用户没有配置信息，或者没有用户组信息都算是没有登录成功。
            return generateHttpErrorRESTFulResponse(HttpStatus.UNAUTHORIZED);
        }
        if (userAcc.getMenus() != null)
        {
            return generateRESTFulResponse(userAcc);
        }
        List<UserAccountRole> roles = userAcc.getAllRoles();
        if (roles.size() == 0)
        {
            logger.info("User: {} does not belong to any role.", userAcc.getLoginName());
            return generateHttpErrorRESTFulResponse(HttpStatus.UNAUTHORIZED);
        }
        uaService.generateUserAccFunctionalities(userAcc, roles);
        uaService.addSuperAdminFlag(userAcc);
        userAcc.setDivisionGroup(null);
        userAcc.setWorkGroups(null);
        userAcc.setOwner(null);
        return generateRESTFulResponse(userAcc);
    }

    @PostMapping(value = "logout")
    public ResponseEntity<ServerResponse<String>> addLogoutAudit(HttpServletRequest request)
    {
        Subject currentUser = SecurityUtils.getSubject();
        String res = "";
        if (currentUser != null && currentUser.isAuthenticated())
        {
            saveLogoutAudit(request);
            currentUser.logout();
            logger.info("Logout successfully.");
            res = "Logout successfully.";
        }
        else
        {
            logger.info("Current user has not been login.");
            res = "Current user has not been login.";
        }
        refreshJavaSessionId(request);
        return generateRESTFulResponse(res);
    }

    private void saveLogoutAudit(HttpServletRequest request)
    {
        String jsessionId = getJsessionId(request);
        if (jsessionId.length() == 32)
        {
            LoginAudit record = loginAuditService.fetchByJseesionId(jsessionId);
            if (record != null)
            {
                record.setLogoutTime(new Date());
                loginAuditService.update(record.getId(), record, CommonConstant.systemUserAccountId,
                        CommonConstant.defaultSystemLanguage);
            }
        }
    }

    private void saveLoginAudit(String loginName, String password, HttpServletRequest request,
            HttpServletResponse response)
    {
        LoginAudit record = new LoginAudit();
        record.setIpAddress(getAccessIP(request));
        record.setLoginName(loginName);
        record.setUsedPasswordHash(password);
        record.setJsessionId(getJsessionId(response, request));
        record.setLoginTime(new Date());
        loginAuditService.create(record, CommonConstant.systemUserAccountId, CommonConstant.defaultSystemLanguage);
    }

    private String getJsessionId(HttpServletResponse response, HttpServletRequest request)
    {
        String cookieString = response.getHeader("Set-Cookie");
        String[] cookies = cookieString.split(";");
        for (String cookie : cookies)
        {
            if (cookie.startsWith("JSESSIONID="))
            {
                return cookie.substring("JSESSIONID=".length());
            }
        }
        return getJsessionId(request);
    }

    private String getJsessionId(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
        {
            if ("JSESSIONID".equals(cookie.getName()))
            {
                return cookie.getValue();
            }
        }
        return "";
    }

    private String getAccessIP(HttpServletRequest request)
    {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null)
        {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
