package com.polarj.common.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.common.web.model.ServerResponse;
import com.polarj.common.web.model.WebErrorStatus;
import com.polarj.model.Functionality;
import com.polarj.model.UserAccount;
import com.polarj.model.enumeration.ModelOperation;
import com.polarj.model.service.FunctionalityService;

@RestController
@RequestMapping("/functionalitys")
public class FunctionalityController extends ModelController<Functionality, Integer, FunctionalityService>
{

    public FunctionalityController()
    {
        super(Functionality.class);
    }

    @PostMapping("fetchAllByOwner")
    public @ResponseBody ResponseEntity<ServerResponse<Functionality>> fetchEntitiesByCriteria()
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.READ.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            List<Functionality> functions = service.listByOwner(userAcc, userAcc.getUserSetting().getWorkLang());
            
            return generateRESTFulResponse(functions);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }
}