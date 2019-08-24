package com.polarj.model.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
// 有这个注解的模型字段，其值需要到resourcei18n表中查得
// 需要配合I18nKeyField才能组成该字段值对应的资源key
public @interface I18nField
{
}
