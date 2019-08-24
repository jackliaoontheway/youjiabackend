package com.polarj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import com.polarj.model.enumeration.FilterOperator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(showDetailFieldName = "FilterStrategyItem")
@Entity
@Table(name = "filterStrategyItem")
public @ToString @EqualsAndHashCode(callSuper = false) class ModelFilterStrategyItem extends GenericDbInfo
{
    private static final long serialVersionUID = -1L;

    // 属性的全名， classFullName + name
    @FieldMetaData(position = 10, label = "Field Full Name")
    @Column(length = 255)
    private @Getter @Setter String fieldFullName;

    // field name for both db and model
    @FieldMetaData(position = 20, label = "Field Name")
    @Column(length = 32)
    private @Getter @Setter String fieldName;

    // 过滤操作符
    @FieldMetaData(position = 30, label = "Filter Operator", enumClass = FilterOperator.class)
    @Column(length = 64)
    private @Getter @Setter String filterOperator;

    // 过滤值
    @FieldMetaData(position = 40, label = "Filter Value", maxLength = 128)
    @Column(length = 200)
    private @Getter @Setter String filterValue;

    // 警告或者错误信息内容
    @FieldMetaData(position = 50, label = "Message")
    @Column(length = 512)
    private @Getter @Setter String message;

    // 过滤条件优先级
    @FieldMetaData(position = 60, label = "Rank", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Getter @Setter Integer rank;
}
