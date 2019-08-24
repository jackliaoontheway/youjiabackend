package com.polarj.model.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.common.CommonConstant;
import com.polarj.common.utility.CryptoUtil;
import com.polarj.model.Functionality;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;
import com.polarj.model.enumeration.FunctionType;
import com.polarj.model.repository.UserAccountRepos;
import com.polarj.model.service.UserAccountRoleService;
import com.polarj.model.service.UserAccountService;

@Service
public class UserAccountServiceImpl extends EntityServiceImpl<UserAccount, Integer> implements UserAccountService
{
    
    @Autowired
    private UserAccountRoleService roleService;
    
    @Override
    public UserAccount fetchByName(String loginName)
    {
        UserAccountRepos repos = (UserAccountRepos) getRepos();
        UserAccount userAcc = repos.findByLoginName(loginName);
        return dataClone(userAcc);
    }

    @Override
    public UserAccount fetchByHashedName(String hashedLoginName)
    {
        UserAccountRepos repos = (UserAccountRepos) getRepos();
        UserAccount userAcc = repos.findByHashedLoginName(hashedLoginName);
        return dataClone(userAcc);
    }
    
    @Override
    public List<UserAccount> listByOwner(UserAccount owner, String languageId)
    {
        UserAccountRepos repos = (UserAccountRepos) getRepos();
        List<UserAccount> list = repos.findByOwner(owner);
        if(list == null) {
            list = new ArrayList<>();
        }
        list.add(owner);
        return list;
    }

    @Override
    public Boolean modifyPassword(String loginName, String originalPassword, String newPassword)
    {
        UserAccount userAcc = fetchByName(loginName);
        if (userAcc == null)
        {
            return false;
        }
        boolean validate =
                CryptoUtil.validatePassword(userAcc.getPasswordHash(), originalPassword, userAcc.getPasswordSalt());
        if (validate)
        {
            userAcc.setPassword(newPassword);
            update(userAcc.getId(), userAcc, userAcc.getId(), CommonConstant.defaultSystemLanguage);
        }
        else
        {
            return false;
        }
        return true;
    }

    @Override
    public UserAccount update(Integer entityId, UserAccount entityWithUpdatedInfo, Integer operId, String languageId)
    {
        UserAccount res = null;
        UserAccountRepos repos = (UserAccountRepos) getRepos();
        if (entityWithUpdatedInfo == null || entityWithUpdatedInfo.getId() == null || entityWithUpdatedInfo.getId() == 0
                || !entityWithUpdatedInfo.getId().equals(entityId))
        {
            logger.info("the updated entity information is incorrect.");
            return res;
        }
        UserAccount existingEntity = repos.getOne(entityId);
        if (existingEntity == null)
        {
            logger.info("No entity found at this moment.");
            return res;
        }

        if (entityWithUpdatedInfo.getPassword() != null && entityWithUpdatedInfo.getNewPassword() != null
                && entityWithUpdatedInfo.getPassword().length() > 0
                && entityWithUpdatedInfo.getNewPassword().length() > 0)
        {
            boolean validate = CryptoUtil.validatePassword(existingEntity.getPasswordHash(),
                    entityWithUpdatedInfo.getPassword(), existingEntity.getPasswordSalt());
            if (validate)
            {
                entityWithUpdatedInfo.setPassword(entityWithUpdatedInfo.getNewPassword());
            }
        }
        return super.update(entityId, entityWithUpdatedInfo, operId, languageId);
    }
    
    public void generateUserAccFunctionalities(UserAccount userAcc, List<UserAccountRole> roles)
    {
        List<Functionality> firstLevelMenus = new ArrayList<>();
        List<Functionality> modelOpers = new ArrayList<>();
        List<Functionality> frontendOpers = new ArrayList<>();

        // 当一个用户有多个角色时 当多个角色同时拥有某些权限时需要去重
        Set<Integer> uniqueFunctionalitiesSet = new TreeSet<>();

        for (UserAccountRole ur : roles)
        {
            UserAccountRole urInfo = roleService.getById(ur.getId(), 3, userAcc.getUserSetting().getViewLang());
            List<Functionality> roleFuncs = urInfo.getFunctions();
            for (Functionality roleFunc : roleFuncs)
            {
                if (uniqueFunctionalitiesSet.contains(roleFunc.getId()))
                {
                    continue;
                }
                uniqueFunctionalitiesSet.add(roleFunc.getId());
                switch (FunctionType.valueOf(roleFunc.getType()))
                {
                case FRONTEND_OPERATION:
                        frontendOpers.add(roleFunc);
                    break;
                case MENU:
                    if (!firstLevelMenus.contains(roleFunc) && roleFunc.getParentMenu() == null)
                    {
                        firstLevelMenus.add(roleFunc);
                    }
                    break;
                case MODEL_OPERATION:
                    modelOpers.add(roleFunc);
                    break;
                default:
                    break;
                }
            }
            // 目前只支持2级菜单 如果要支持3级菜单 参考 UserAccountRoleServiceImpl groupByMenu
            for (Functionality firstLevelMenu : firstLevelMenus)
            {
                for (Functionality roleFunc : roleFuncs)
                {
                    if (roleFunc.getParentMenu() != null
                            && roleFunc.getParentMenu().getId().equals(firstLevelMenu.getId()))
                    {
                        firstLevelMenu.addMenu(roleFunc);
                    }
                }
            }
        }
        userAcc.setMenus(firstLevelMenus);
        userAcc.setFrontendOperations(frontendOpers);
        userAcc.setModelOperations(modelOpers);
    }
    
    public void addSuperAdminFlag(UserAccount userAcc)
    {
        List<UserAccountRole> roles = userAcc.getAllRoles();
        for(UserAccountRole role: roles)
        {
            if(role.getCode().equals("admin"))
            {
                userAcc.setSuperAdmin(true);
                break;
            }
        }
    }

}
