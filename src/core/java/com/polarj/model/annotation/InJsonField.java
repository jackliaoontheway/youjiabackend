package com.polarj.model.annotation;

// 这是@JsonView注解的参数标志
// 说明业务模型的带有该标志的属性保存到一个json字段中来进行持久化
// 使用@JsonView(InJsonField.class)来注解业务模型的属性
public interface InJsonField
{
}
