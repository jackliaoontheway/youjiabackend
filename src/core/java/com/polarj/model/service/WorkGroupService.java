package com.polarj.model.service;

import com.polarj.model.GroupModelView;
import com.polarj.model.WorkGroup;

public interface WorkGroupService extends EntityService<WorkGroup, Integer>
{

    Boolean addUserToWorkGroup(GroupModelView data, Integer operatorId, String viewLang);
}