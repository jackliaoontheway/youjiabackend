package com.polarj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 符合ISO 4217标准的货币代码和名称
@ModelMetaData(showDetailFieldName="code")
@Entity
@Table(name = "isocurrency", indexes = { @Index(columnList = "code", name = "UK_Currency_Code", unique = true) })
public @ToString @EqualsAndHashCode(callSuper = false) class CurrencyISO4217 extends GenericDbInfo
{
    private static final long serialVersionUID = -7733768034705943237L;

    @FieldMetaData(position = 10, label = "Code", required = true, maxLength = 8)
    @I18nKeyField
    @Column(length = 8)
    private @Getter @Setter String code;

    @FieldMetaData(position = 20, label = "Name", required = true, maxLength = 250)
    @I18nField
    @Column(length = 255)
    private @Getter @Setter String name;
    
    // QUES： 是否需要把货币符号与货币符号的位置也持久化？
}
