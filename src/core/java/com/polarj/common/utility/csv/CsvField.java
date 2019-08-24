package com.polarj.common.utility.csv;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

// 用于产生csv字符串时的描述，目前只用于基本数据类型的注解
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface CsvField
{
    // 是csv数据记录的第几个属性，
    int sn();

    // 数字或者日期属性的数据显示格式
    String fmt() default "";
}
