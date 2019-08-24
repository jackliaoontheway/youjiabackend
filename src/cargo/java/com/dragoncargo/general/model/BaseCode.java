package com.dragoncargo.general.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  基础basecode
 */
@Entity
@Table(name = "basecode")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "classification")
public @ToString @EqualsAndHashCode(callSuper = false) class BaseCode extends GenericDbInfo
{

    /**
     * 
     */
    private static final long serialVersionUID = -3697305539278453631L;

    @I18nKeyField
    @FieldMetaData(position = 10, label = "Code")
    @Column(length = 64)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 20, label = "Name")
    @Column(length = 64)
    private @Setter @Getter String name;

    @FieldMetaData(position = 20, label = "Description")
    @Column(length = 200)
    private @Setter @Getter String description;
}
