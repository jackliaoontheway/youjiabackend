package com.polarj.model.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// 描述业务模型的可操作性描述
// QUES：是不是能增加自定义的操作呢？达到功能在后端定义好，前端只是显示出来。
// 目前的实现方案是：前端自定义。分成记录级别的操作和模型级别的操作两种。
@Retention(RUNTIME)
@Target(TYPE)
public @interface ModelMetaData
{
    // 对应前端可以使用的icon名称
    String iconName() default "";

    // 在业务模型管理的时候，作为前端当前页面的标题
    String label() default "";

    // 前端显示在标题处的搜索内容，如果不打开高级搜索，那么就是搜索这个字段的内容
    // QUES： 目前支持一个字段，可以考虑支持多个字段
    // 暂时用“,”分开各个字段的名称
    String searchField() default "";

    // 缺省的排序属性，在列表显示的时候，用于排序的属性
    String indexField() default "";

    // 业务模型的数据按该注解的属性进行分类显示
    String tabField() default "";

    // 用于分类的属性中，哪些值出现在分类中
    String[] tabValues() default {};

    // 升序还是降序, 缺省为升序
    boolean sortDesc() default false;

    boolean updatable() default true;

    boolean deletable() default true;

    boolean batchDeletable() default true;

    boolean addible() default true;

    boolean downloadable() default true;

    boolean uploadable() default true;

    int level() default 1;

    String showDetailFieldName() default "";

    // 描述该业务模型会处于那个工作流的管理之下，
    // 目前是只支持一个工作流
    // 需要和工作流数据库配置中的名字一致
    // QUES：设置为数组是为支持多个工作流留下余地
    String[] workflowName() default {};
}
