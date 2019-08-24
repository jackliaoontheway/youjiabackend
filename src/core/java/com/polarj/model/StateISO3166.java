package com.polarj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//符合ISO 3166标准的省州一级单位的列表
@Entity
@Table(name = "isostate", indexes = { @Index(columnList = "code", name = "UK_State_Code", unique = true) })
public @ToString @EqualsAndHashCode(callSuper = false) class StateISO3166 extends GenericDbInfo
{
    private static final long serialVersionUID = 3429750044077962060L;

    @FieldMetaData(position = 10, label = "Code", required = true, maxLength = 8)
    @I18nKeyField
    @Column(length = 8)
    private @Getter @Setter String code;

    @FieldMetaData(position = 20, label = "Name", required = true, maxLength = 250)
    @I18nField
    @Column(length = 255)
    private @Getter @Setter String name;

}
