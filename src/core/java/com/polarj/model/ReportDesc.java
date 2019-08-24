package com.polarj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.ReportType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(showDetailFieldName="code")
@Entity
@Table(name = "reportdesc", indexes = { @Index(columnList = "code", name = "UK_ReportDesc_Code", unique = true) })
public @ToString @EqualsAndHashCode(callSuper = false) class ReportDesc extends GenericDbInfo
{
    private static final long serialVersionUID = -6574196604537055179L;

    @I18nKeyField
    @FieldMetaData(position = 20, label = "Code", required = true, maxLength = 128)
    @Column(name = "code", length = 128, nullable = false)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 30, label = "Name", required = true, maxLength = 128)
    @Column(name = "name", length = 128)
    private @Getter @Setter String name;

    @I18nField
    @FieldMetaData(position = 40, label = "Description", required = true, maxLength = 1024)
    @Column(name = "description", length = 1024)
    private @Getter @Setter String description;

    @FieldMetaData(position = 45, label = "Type", enumClass = ReportType.class, required = true, maxLength = 64)
    @Column(name = "type", length = 64, nullable = false)
    private @Getter @Setter String type;

    @FieldMetaData(position = 50, label = "Implementation", required = true, maxLength = 255)
    @Column(name = "implementationClass", length = 255, nullable = false)
    private @Getter @Setter String implementationClass;

    @FieldMetaData(position = 60, label = "Criteria", required = true, maxLength = 255)
    @Column(name = "criteriaClass", length = 255, nullable = false)
    private @Getter @Setter String criteriaClass;

    @FieldMetaData(position = 70, label = "Result Class", required = true, maxLength = 255)
    @Column(name = "resultClass", length = 255, nullable = false)
    private @Getter @Setter String resultClass;//TODO List of mode view field
}
