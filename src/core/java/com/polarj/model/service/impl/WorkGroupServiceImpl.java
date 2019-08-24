package com.polarj.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.model.GroupModelView;
import com.polarj.model.UserAccount;
import com.polarj.model.WorkGroup;
import com.polarj.model.repository.WorkGroupRepos;
import com.polarj.model.service.UserAccountService;
import com.polarj.model.service.WorkGroupService;

@Service
public class WorkGroupServiceImpl extends EntityServiceImpl<WorkGroup, Integer> implements WorkGroupService
{

    @Autowired
    private UserAccountService userAccountService;

    @Override
    public List<WorkGroup> listByOwner(UserAccount owner, String languageId)
    {
        WorkGroupRepos repos = (WorkGroupRepos) getRepos();
        return repos.findByOwner(owner);
    }

    @Override
    @Transactional
    public Boolean addUserToWorkGroup(GroupModelView data, Integer operatorId, String languageId)
    {
        if (data == null)
        {
            return false;
        }
        String loginName = data.getLabel();
        Integer userGroupId = data.getId();

        UserAccount userAccount = userAccountService.fetchByName(loginName);

        if (userAccount == null)
        {
            return false;
        }

        WorkGroup workGroup = this.getById(userGroupId, languageId);

        boolean hasPermission = false;
        if (workGroup != null && workGroup.getOwner() != null
                && workGroup.getOwner().getId().equals(operatorId))
        {
            hasPermission = true;
        }
        else if (workGroup != null && workGroup.getGroupManager() != null
                && workGroup.getGroupManager().getId().equals(operatorId))
        {
            hasPermission = true;
        }
        if (hasPermission)
        {
            List<UserAccount> userAccountList = workGroup.getUserAccountList();
            if (userAccountList == null)
            {
                userAccountList = new ArrayList<>();
                workGroup.setUserAccountList(userAccountList);
            }
            userAccountList.add(userAccount);
            this.update(userGroupId, workGroup, operatorId, languageId);
            return true;
        }

        return false;
    }

}
