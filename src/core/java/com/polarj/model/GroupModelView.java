package com.polarj.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class GroupModelView
{
    private @Setter @Getter Integer id;

    private @Setter @Getter Integer positionSn;

    private @Setter @Getter String label;

    private @Setter @Getter boolean groupType = true;

    private @Setter @Getter boolean selected;

    private @Setter @Getter List<GroupModelView> subGroups;

    private void addSubGroup(GroupModelView userGroupModelView)
    {
        if (subGroups == null)
        {
            subGroups = new ArrayList<>();
        }
        subGroups.add(userGroupModelView);
    }

    private GroupModelView buildUserGroupModelView(UserAccount userAccount)
    {
        GroupModelView modelView = new GroupModelView();
        modelView.setPositionSn(userAccount.getId());
        modelView.setLabel(userAccount.getLoginName());
        modelView.setGroupType(false);
        return modelView;
    }

    public void addSubGroup(UserAccount userAccount)
    {
        if (userAccount != null)
        {
            GroupModelView accountModelView = buildUserGroupModelView(userAccount);
            this.addSubGroup(accountModelView);
        }

    }

    public void addSubGroups(List<UserAccount> userAccountList)
    {
        if (userAccountList != null && userAccountList.size() > 0)
        {
            for (UserAccount userAccount : userAccountList)
            {
                GroupModelView accountModelView = buildUserGroupModelView(userAccount);
                this.addSubGroup(accountModelView);
            }
        }

    }

}
