package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.UserAccountRole;
import com.polarj.model.service.UserAccountRoleService;

@RestController
@RequestMapping("/roles")
public class RoleController extends UserRestrictionModelController<UserAccountRole, Integer, UserAccountRoleService>
{

    public RoleController()
    {
        super(UserAccountRole.class);
    }
}