package com.polarj.model;

// 实现该接口的业务模型（简称该类），具有如下属性
// 1. 其子类的所有属性值都保存在该类的jsonFields属性中
// 2. 其所有子类的ModelSpecification并持久化，但是会传输到前端
// 3. 该类的所有子类的ModelSpecification有两个属性值与该类不同（className和label）
// 4. 该类与其子类的关系保存在ModelInheritanceRel中
// 5. 如果该类是非抽象类，则该类与自身也是ModelInheritanceRel中的一条记录
// 6. 图过该类是抽象类，那么在ModelInheritanceRel中没有记录，该类也不能在前端操作（QUES：怎么实现呢？）
public interface HasSubWithFieldInJson extends HasFieldInJson
{
    void setSubClassName(String subClassName);

    String getSubClassName();
}
