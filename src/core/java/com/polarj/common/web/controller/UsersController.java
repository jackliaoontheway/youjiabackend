package com.polarj.common.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.common.CommonConstant;
import com.polarj.common.security.JWTUtils;
import com.polarj.common.utility.CryptoUtil;
import com.polarj.common.web.model.ClientRequest;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.common.web.model.WebErrorStatus;
import com.polarj.common.web.service.InitialRegisteredUserDataService;
import com.polarj.common.web.service.UserSelfServiceService;
import com.polarj.model.Functionality;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountConfig;
import com.polarj.model.UserAccountRole;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.UserAccountStatus;
import com.polarj.model.service.UserAccountRoleService;
import com.polarj.model.service.UserAccountService;

@RestController
@RequestMapping("/useraccounts")
public class UsersController extends UserRestrictionModelController<UserAccount, Integer, UserAccountService>
{
    @Autowired
    private UserSelfServiceService selfService;

    @Autowired
    private UserAccountRoleService roleService;

    @Autowired(required = false)
    private InitialRegisteredUserDataService initialService;

    public UsersController()
    {
        super(UserAccount.class);
    }

    @PostMapping("register")
    public @ResponseBody ResponseEntity<ServerResponse<UserAccount>>
            register(@RequestBody ClientRequest<UserAccount> clientRequest)
    {
        try
        {
            UserAccount model = clientRequest.getData();
            UserAccount existingUa = service.fetchByName(model.getLoginName());
            if (existingUa != null)
            {
                return generateErrorRESTFulResponse("该用户已经存在或者其他原因不能使用该邮件注册用户。");
            }
            // if (!model.valid())
            // {
            // return generateErrorRESTFulResponse("invalid information.");
            // }
            UserAccountRole defaultRole = roleService.fetchDefaultRole();
            if (defaultRole == null)
            {
                logger.error("No defaultRole in the system.");
            }
            else
            {
                UserAccountConfig uac = new UserAccountConfig();
                uac.copy(defaultRole.getDefaultSetting());
                model.setUserSetting(uac);
            }
            model.addRole(defaultRole);
            UserAccount result = null;
            result = service.create(model, CommonConstant.systemUserAccountId, CommonConstant.defaultSystemLanguage);
            if (wfViewService != null && result != null)
            {
                wfViewService.workflowForCreatingModel(result, service, CommonConstant.systemUserAccountId);
            }
            selfService.sendRegisterConfirmationEmail(getFrontendBaseURL(), result);
            return generateRESTFulResponse(result);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("confirm") // url: confirm?loginName=&nonLoginToken=
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>> confirmRegistration(
            @RequestParam(name = "loginName", required = true) String loginName,
            @RequestParam(name = "nonLoginToken", required = true) String nonLoginToken)
    {
        try
        {
            UserAccount model = service.fetchByName(loginName);
            Boolean result = Boolean.FALSE;
            if (model != null && model.getStatus().equals(UserAccountStatus.PENDING.name())
                    && model.getNonLoginToken().equals(nonLoginToken))
            {
                model.setStatus(UserAccountStatus.ACTIVE.name());
                model.setNonLoginToken(null);
                result = Boolean.TRUE;
                if (initialService != null)
                {
                    initialService.initialRegisteredUserData(model);
                }
                service.update(model.getId(), model, CommonConstant.systemUserAccountId,
                        CommonConstant.defaultSystemLanguage);
            }
            return generateRESTFulResponse(result);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("reset-password-request") // url: reset-password-request?loginName=
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>>
            resetPasswordRequest(@RequestParam(name = "loginName", required = true) String loginName)
    {
        try
        {
            UserAccount model = service.fetchByName(loginName);
            Boolean result = Boolean.FALSE;
            // 只有合法可以使用的帐号才能重新设置密码， 否则需要联系系统管理员
            if (model != null && model.getStatus().equalsIgnoreCase(UserAccountStatus.ACTIVE.name()))
            {
                model.setStatus(UserAccountStatus.LOCKED.name());
                model.setNonLoginToken();
                UserAccount ua = service.update(model.getId(), model, CommonConstant.systemUserAccountId,
                        CommonConstant.defaultSystemLanguage);
                if (ua != null)
                {
                    result = Boolean.TRUE;
                }
            }
            if (result)
            {
                selfService.sendResetPasswordEmail(getFrontendBaseURL(), model);
            }
            return generateRESTFulResponse(result);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("reset-password") // url: reset-password?loginName=&nonLoginToken=&newPassword=
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>> resetPassword(
            @RequestParam(name = "loginName", required = true) String loginName,
            @RequestParam(name = "nonLoginToken", required = true) String nonLoginToken,
            @RequestParam(name = "newPassword", required = true) String newPassword)
    {
        try
        {
            UserAccount model = service.fetchByName(loginName);
            Boolean result = Boolean.FALSE;
            if (model != null && model.getStatus().equals(UserAccountStatus.LOCKED.name())
                    && model.getNonLoginToken().equals(nonLoginToken))
            {
                model.setStatus(UserAccountStatus.ACTIVE.name());
                model.setNonLoginToken(null);
                model.setPassword(newPassword);
                result = Boolean.TRUE;
                service.update(model.getId(), model, CommonConstant.systemUserAccountId,
                        CommonConstant.defaultSystemLanguage);
            }
            return generateRESTFulResponse(result);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("{id}")
    public @ResponseBody ResponseEntity<ServerResponse<UserAccount>> updateEntity(@PathVariable Integer id,
            @RequestBody ClientRequest<UserAccount> clientRequest)
    {
        ResponseEntity<ServerResponse<UserAccount>> res = super.updateEntity(id, clientRequest);

        if (res.getBody() != null && res.getBody().fetchOneData() != null)
        {
            UserAccount userAcc = getLoginUserAccount();
            if (userAcc.getId().equals(res.getBody().fetchOneData().getId()))
            {
                // QUES: 因为登录用户的配置数据可能会被改变，需要把个人配置数据更新，目前先全部更新
                // 如果是密码改变，则需要登出系统重新登录
                if (userAcc.getUserSetting() != null)
                {
                    userAcc.getUserSetting().copy(res.getBody().fetchOneData().getUserSetting());
                }
                userAcc.setMenus(null);
                userAcc.setModelOperations(null);
                userAcc.setFrontendOperations(null);
            }
        }
        return res;
    }

    @GetMapping("{id}/functionalities")
    public ResponseEntity<ServerResponse<Functionality>> listMenus(@PathVariable Integer id)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        try
        {
            List<Functionality> firstLevelMenus = userAcc.getMenus();
            return generateRESTFulResponse(firstLevelMenus);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("{id}/generateapitoken")
    public @ResponseBody ResponseEntity<ServerResponse<UserAccount>> generateAPIToken(@PathVariable Integer id)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        try
        {
            UserAccount u = service.getById(id, userAcc.getUserSetting().getViewLang());
            u.setApiPassword(CryptoUtil.generateSalt());
            String apiToken = JWTUtils.sign(u.getHashedLoginName(), u.getApiPassword(), logger);
            u.setApiToken(apiToken);
            UserAccount u2 = service.update(id, u, id, userAcc.getUserSetting().getViewLang());
            return generateRESTFulResponse(u2);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("changestatus") // url: changestatus?action=&id=
    public ResponseEntity<ServerResponse<Boolean>> changeUserAccountStatus(
            @RequestParam(name = "action", required = true) String action,
            @RequestParam(name = "id", required = true) int id)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        try
        {
            UserAccount u = service.getById(id, userAcc.getUserSetting().getViewLang());
            ModelMetaData mmd = u.getClass().getAnnotation(ModelMetaData.class);
            if (wfViewService != null && mmd != null && mmd.workflowName().length > 0)
            {
                wfViewService.workflowForUserOperation(u, action, userAcc.getId());
            }
            return generateRESTFulResponse(Boolean.TRUE);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }
}