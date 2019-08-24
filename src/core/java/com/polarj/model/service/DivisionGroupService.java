package com.polarj.model.service;

import java.util.List;

import com.polarj.model.DivisionGroup;
import com.polarj.model.UserAccount;
import com.polarj.model.GroupModelView;

public interface DivisionGroupService extends EntityService<DivisionGroup, Integer>
{

    List<GroupModelView> fetchDivisionGroups(UserAccount userAcc, String viewLang);

    Boolean addUserToDivisionGroup(GroupModelView data, Integer operatorId, String viewLang);
}