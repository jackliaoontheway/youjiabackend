package com.polarj.model.service;

import com.polarj.model.HasFieldInJson;

//msg :不将此方法放在EntityService，是考虑到不是所有model都继承ModelWithChangeableField的，为了解耦，单独做成接口
// 添加接口ChangableFieldEntityService
// 用于处理带jsonfield扩展类的子类不带用户权限更新保存数据，对应的server接口需要继承此接口
// 由于每个model保存config子类名的字段会不一样,不统一,
// 所以对应的model实现类需要重写一遍此方法， 获取对应jsonview数据实际使用的子类,返回子类名

public  interface  ChangableFieldEntityService
{

    public <T extends HasFieldInJson> String getConfigClass(T entity);
}
