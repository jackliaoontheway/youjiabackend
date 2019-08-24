package com.polarj.common.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.common.web.model.ClientRequest;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.common.web.model.WebErrorStatus;
import com.polarj.model.FunctionalityModelView;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;
import com.polarj.model.service.UserAccountRoleService;

@RestController
@RequestMapping("/useraccountroles")
public class UserAccountRoleController extends ModelController<UserAccountRole, Integer, UserAccountRoleService>
{

    public UserAccountRoleController()
    {
        super(UserAccountRole.class);
    }

    @PostMapping("fetchFunctionalitiesByUserRole")
    public @ResponseBody ResponseEntity<ServerResponse<FunctionalityModelView>>
            fetchFunctionalitiesByUserRole(@RequestBody ClientRequest<UserAccountRole> clientRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted("GrantFuncationalityToUserRole"))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            if (clientRequest != null && clientRequest.getData() != null)
            {
                UserAccountRole userAccountRole = clientRequest.getData();
                List<FunctionalityModelView> functions = service.fetchFunctionalitiesByUserRole(userAcc,
                        userAccountRole.getId(), userAcc.getUserSetting().getViewLang());
                return generateRESTFulResponse(functions);
            }
            return generateRESTFulResponse(new ArrayList<>());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("grantFunctionalityForUserRole")
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>>
            grantFunctionalityForUserRole(@RequestBody ClientRequest<List<FunctionalityModelView>> clientRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted("GrantFuncationalityToUserRole"))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            if (clientRequest != null && clientRequest.getData() != null)
            {
                List<FunctionalityModelView> functionalities = clientRequest.getData();
                Boolean result =
                        service.grantFunctionalityForUserRole(functionalities, userAcc.getId(), userAcc.getUserSetting().getViewLang());
                return generateRESTFulResponse(result);
            }
            return generateRESTFulResponse(new ArrayList<>());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

}