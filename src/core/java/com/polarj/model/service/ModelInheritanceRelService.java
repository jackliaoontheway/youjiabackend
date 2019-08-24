package com.polarj.model.service;

import java.util.List;

import com.polarj.model.ModelInheritanceRel;

public interface ModelInheritanceRelService extends EntityService<ModelInheritanceRel, Integer>
{
    // 获取className的所有需要管理的子类信息（也包括自己）
    List<ModelInheritanceRel> fetchModelInheritanceRelByBaseClassName(String baseClassName);

    ModelInheritanceRel fetchByUniqueData(String baseClassName, String subClassName);
}
