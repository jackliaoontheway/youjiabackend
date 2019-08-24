package com.polarj.model;

// 实现这个接口的业务模型具有如下特点：
//   1. 一定有一个持久化的jsonFields属性，但是并不把数据传到前端
//   2. jsonFields属性中保存的是所有该业务模型中被@InJsonField注解的属性
//
// 局限性：
//   1. 如果父类已经实现了该接口，那么子类的所有属性必须被@InJsonField注解，同时子类不能单独的持久化
//   2. 只针对简单数据类型才能使用@InJsonField注解
//   3. 在Json中保存的数据，暂时不能被查询（即：不能设置fieldmetadata的sortable为true）。
//      可以想到的查询方式为：保存在json中的属性作为一个字段统一全文查询
//   4. json中保存的数据，不能被用于ModelMetaData中的searchField，indexField，tabField，showDetailFieldName等属性的值
//
// 使用方式：
//   1. 如果实现类及其子类都是单独管理的，则直接针对每一个需要独立管理的业务模型建
//      立前后端管理接口，同时按照FieldMetaData等在前端呈现
//   2. QUES：如果实现类本身是单独管理，所有子类都以父类为基础一起管理，应该有一个单选所有子类的列表，根据列表再呈现相应的子类属性
public interface HasFieldInJson
{
    String getJsonFields();

    <T extends HasFieldInJson> void setJsonFields(String jsonFields);

    // 把当前业务模型的数据加上jsonFields里面的数据一起转换成子类的类型数据
    <T extends HasFieldInJson, D extends T> D castTo(Class<D> clazz);
}
