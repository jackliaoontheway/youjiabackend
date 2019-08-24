package com.polarj.model.service;

import java.util.List;

import com.polarj.model.ModelSpecification;

public interface ModelSpecificationService extends EntityService<ModelSpecification, Integer>
{
    // public void loadModelSpecificationAutomatically(Integer operId);

    // 直接从数据库获取信息
    public ModelSpecification fetchModelSpecificationByClassName(String className, String languageId);

    // 如果对应的业务模型实现了HasSubWithFieldInJson接口，那么这是对一组具有继承关系的类的集中管理
    // 所以返回是一系列的模型属性说明
    public List<ModelSpecification> fetchAllSubModelSpecificationByClassName(String className, String languageId);

    // 从业务模型的ModelMetaData注解生成ModelSpecification
    public ModelSpecification generateModelSpecificationByModelMetaData(Class<?> modelClazz, String languageId);
}
