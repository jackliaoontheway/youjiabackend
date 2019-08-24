package com.polarj.model.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.model.DivisionGroup;
import com.polarj.model.GroupModelView;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountGroup;
import com.polarj.model.repository.DivisionGroupRepos;
import com.polarj.model.service.DivisionGroupService;
import com.polarj.model.service.UserAccountService;

@Service
public class DivisionGroupServiceImpl extends EntityServiceImpl<DivisionGroup, Integer> implements DivisionGroupService
{
    private final static int MAX_GROUP_LEVEL = 5; // 最多5层分组

    @Autowired
    private UserAccountService userAccountService;

    @Override
    public List<DivisionGroup> listByOwner(UserAccount owner, String languageId)
    {
        DivisionGroupRepos repos = (DivisionGroupRepos) getRepos();
        return repos.findByOwner(owner);
    }

    @Override
    @Transactional(value = TxType.NOT_SUPPORTED)
    public List<GroupModelView> fetchDivisionGroups(UserAccount userAcc, String languageId)
    {
        List<DivisionGroup> userGroups = listByOwner(userAcc, languageId);

        List<GroupModelView> firstGroups = new ArrayList<>();
        for (DivisionGroup userGroup : userGroups)
        {
            if (userGroup.getParentDivision() == null)
            {
                GroupModelView modelView = buildUserGroupModelView(userGroup);

                Set<Integer> subUserAccountIdSet = new TreeSet<>();
                modelView.setSubGroups(findSubGroups(subUserAccountIdSet, null, userGroup, userGroups, 1));

                if (userGroup.getUserAccountList() != null)
                {
                    for (UserAccount userAccount : userGroup.getUserAccountList())
                    {
                        if (!subUserAccountIdSet.contains(userAccount.getId()))
                        {
                            modelView.addSubGroup(userAccount);
                        }
                    }
                }

                firstGroups.add(modelView);
            }
        }

        sortByPositionSn(firstGroups);
        return firstGroups;
    }

    private GroupModelView buildUserGroupModelView(UserAccountGroup userGroup)
    {
        GroupModelView modelView = new GroupModelView();
        modelView.setId(userGroup.getId());
        modelView.setPositionSn(userGroup.getPositionSn());
        modelView.setLabel(userGroup.getLabel());
        return modelView;
    }

    private void sortByPositionSn(List<GroupModelView> list)
    {
        list.sort(new Comparator<GroupModelView>()
        {
            @Override
            public int compare(GroupModelView o1, GroupModelView o2)
            {
                Integer firstSn = (o1.getPositionSn() == null ? 0 : o1.getPositionSn());
                Integer secondSn = (o2.getPositionSn() == null ? 0 : o2.getPositionSn());
                return firstSn.compareTo(secondSn);
            }
        });
    }

    private List<GroupModelView> findSubGroups(Set<Integer> allSubUserAccountIdSet, Set<Integer> subUserAccountIdSet,
            DivisionGroup parentGroup, List<DivisionGroup> userGroups, int i)
    {
        if (i++ == MAX_GROUP_LEVEL)
        {
            return null;
        }

        List<GroupModelView> subGroups = new ArrayList<>();
        for (DivisionGroup userGroup : userGroups)
        {
            if (userGroup.getParentDivision() != null
                    && parentGroup.getId().equals(userGroup.getParentDivision().getId()))
            {
                GroupModelView modelView = buildUserGroupModelView(userGroup);

                Set<Integer> newSubUserAccountIdSet = new TreeSet<>();
                modelView.setSubGroups(
                        findSubGroups(allSubUserAccountIdSet, newSubUserAccountIdSet, userGroup, userGroups, i));

                if (userGroup.getUserAccountList() != null)
                {
                    for (UserAccount userAccount : userGroup.getUserAccountList())
                    {
                        allSubUserAccountIdSet.add(userAccount.getId());
                        if (!newSubUserAccountIdSet.contains(userAccount.getId()))
                        {
                            modelView.addSubGroup(userAccount);
                            newSubUserAccountIdSet.add(userAccount.getId());
                        }
                    }
                }

                subGroups.add(modelView);
            }
        }
        sortByPositionSn(subGroups);
        return subGroups;
    }

    @Override
    @Transactional
    public Boolean addUserToDivisionGroup(GroupModelView data, Integer operatorId, String languageId)
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

        DivisionGroup divisionGroup = this.getById(userGroupId, languageId);

        boolean hasPermission = false;
        if (divisionGroup != null && divisionGroup.getOwner() != null
                && divisionGroup.getOwner().getId().equals(operatorId))
        {
            hasPermission = true;
        }
        else if (divisionGroup != null && divisionGroup.getGroupManager() != null
                && divisionGroup.getGroupManager().getId().equals(operatorId))
        {
            hasPermission = true;
        }
        if (hasPermission)
        {
            userAccount.setDivisionGroup(divisionGroup);
            userAccountService.update(userAccount.getId(), userAccount, operatorId, languageId);
            return true;
        }
        return false;
    }

}
