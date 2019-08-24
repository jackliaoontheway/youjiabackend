package com.polarj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;

import lombok.Getter;
import lombok.Setter;

// 持久化实现HasSubWithFieldInJson接口的父类与子类的关系
// 用于枚举子类用于前端选择需要呈现的数据
// 初始化的时候，只要是实现了HasSubWithFieldInJson的业务模型类
// 找到其每一个子类，先判断是否在数据库中存在，不存在的话就增加一条记录
// QUES:如果在初始化的时候没有找到其子类，也增加一条没有subClassName的记录？
// QUES:这个持久化数据不能在前端操作？
@Entity
@Table(name = "modelinheritrel", indexes = { @Index(columnList = "baseClassName, subClassName",
        name = "UK_ModelInheritanceRel_baseClassName_subClassName", unique = true) })
public class ModelInheritanceRel extends GenericDbInfo
{
    private static final long serialVersionUID = -3348576178715949272L;

    // 实现了HasSubWithFieldInJson接口的父类全名
    @Column(length = 250)
    private @Getter @Setter String baseClassName;

    // baseClassName的子类全名，这个是唯一的。QUES：不一定是直接子类
    @I18nKeyField
    @Column(length = 250)
    private @Getter @Setter String subClassName;

    // 子类的显示名，需要国际化，用于在前端选择，编辑的时候确定使用哪些属性，显示的时候确定显示哪些内容
    @I18nField
    @Column(length = 64)
    private @Getter @Setter String label;
}
