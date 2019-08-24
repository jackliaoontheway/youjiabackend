package com.polarj.model.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

//标志注解，每个模型只应该有一个，标注在具有非空唯一值（不要使用数据库模型中的id）的
//模型字段上， 该字段的值提供给其他字段组成国际化的key
@Retention(RUNTIME)
@Target(FIELD)
public @interface I18nKeyField
{

}
