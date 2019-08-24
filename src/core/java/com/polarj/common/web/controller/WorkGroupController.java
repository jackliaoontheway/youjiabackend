package com.polarj.common.web.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.common.web.model.ClientRequest;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.common.web.model.WebErrorStatus;
import com.polarj.model.GroupModelView;
import com.polarj.model.UserAccount;
import com.polarj.model.WorkGroup;
import com.polarj.model.service.WorkGroupService;

@RestController
@RequestMapping("/workgroups")
public class WorkGroupController extends UserRestrictionModelController<WorkGroup, Integer, WorkGroupService>
{

    public WorkGroupController()
    {
        super(WorkGroup.class);
    }

    @PostMapping("addUserToWorkGroup")
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>>
            addUserToWorkGroup(@RequestBody ClientRequest<GroupModelView> clientRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted("addUserToWorkGroup"))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            if (clientRequest != null && clientRequest.getData() != null)
            {
                Boolean result = service.addUserToWorkGroup(clientRequest.getData(), userAcc.getId(),
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