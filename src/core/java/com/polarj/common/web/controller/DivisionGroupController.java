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
import com.polarj.model.DivisionGroup;
import com.polarj.model.GroupModelView;
import com.polarj.model.UserAccount;
import com.polarj.model.service.DivisionGroupService;

@RestController
@RequestMapping("/divisiongroups")
public class DivisionGroupController
        extends UserRestrictionModelController<DivisionGroup, Integer, DivisionGroupService>
{

    public DivisionGroupController()
    {
        super(DivisionGroup.class);
    }

    @PostMapping("fetchDivisionGroups")
    public @ResponseBody ResponseEntity<ServerResponse<GroupModelView>> fetchUserGroups()
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted("DivisionManagement"))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            List<GroupModelView> userAccountGroups =
                    service.fetchDivisionGroups(userAcc, userAcc.getUserSetting().getViewLang());
            return generateRESTFulResponse(userAccountGroups);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("addUserToDivisionGroup")
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>>
            addUserToDivisionGroup(@RequestBody ClientRequest<GroupModelView> clientRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted("AddUserToDivisionGroup"))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            if (clientRequest != null && clientRequest.getData() != null)
            {
                Boolean result = service.addUserToDivisionGroup(clientRequest.getData(), userAcc.getId(),
                        userAcc.getUserSetting().getViewLang());
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