package com.polarj.model.service;

import java.util.List;

import com.polarj.model.FieldSpecification;
import com.polarj.model.UserAccount;

public interface FieldSpecificationService extends EntityService<FieldSpecification, Integer>
{
    public void refreshFieldSpecificationForModelPackages(UserAccount userAcc, String languageId);

    // 从数据库获取某个业务模型的所有需要在前端操作的属性的描述
    public List<FieldSpecification> fetchByModelName(String modelName, String languageId);

    // 查找某一个指定模型属性的描述
    public FieldSpecification fetchByUniqueData(String fieldFullName, String languageId);

    // 给定一个业务模型的类，找出其所有的被FieldMetaData注解的属性
    public List<FieldSpecification> generateFieldSpecificationByFieldMetaData(Class<?> modelClazz, String languageId);
}
