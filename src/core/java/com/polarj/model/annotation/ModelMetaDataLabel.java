package com.polarj.model.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// 如果一个业务模型是另外一个被（）注解的业务模型的子类，
// 使用这个注解来描述初始化的名字，用于前端操作的时候选择正确的业务模型子类
@Retention(RUNTIME)
@Target(TYPE)
public @interface ModelMetaDataLabel
{
    String label();
}
