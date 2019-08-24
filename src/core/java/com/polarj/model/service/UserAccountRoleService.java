package com.polarj.model.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.polarj.model.FunctionalityModelView;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;

public interface UserAccountRoleService extends EntityService<UserAccountRole, Integer>
{
    // FIXME: 目前的唯一性，貌似改为了所有者和code一起，那么这儿就需要在增加所有者信息吧？
    @Transactional
    UserAccountRole fetchByRoleCode(String userRoleCode);

    @Transactional
    UserAccountRole fetchDefaultRole();

    List<FunctionalityModelView> fetchFunctionalitiesByUserRole(UserAccount userAcc,Integer userRoleId, String workLang);

    Boolean grantFunctionalityForUserRole(List<FunctionalityModelView> functionalities,Integer operatorId, String viewLang);
}
