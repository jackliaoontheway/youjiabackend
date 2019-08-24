package com.dragoncargo.omm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 报价项目编码
 */
@ModelMetaData
@Entity
@Table(name = "chargerateitem")
@JsonIgnoreProperties("hibernateLazyInitializer")
public @ToString @EqualsAndHashCode(callSuper = false) class ChargeRateItem extends GenericDbInfo
{

    /**
     * 
     */
    private static final long serialVersionUID = 890315275159736832L;

    @I18nKeyField
    @FieldMetaData(position = 10, label = "Code")
    @Column(length = 64)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 20, label = "Name")
    @Column(length = 64)
    private @Setter @Getter String name;

    @FieldMetaData(position = 30, label = "Charge Rate Category", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = ChargeRateItemCategory.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "chargeRateCategoryId",
            foreignKey = @ForeignKey(name = "fk_chargerateitem_chargeratecategory_chargeratecategoryid"),
            referencedColumnName = "id")
    private @Setter @Getter ChargeRateItemCategory chargeRateCategory;

    @FieldMetaData(position = 40, label = "Description")
    @Column(length = 200)
    private @Setter @Getter String description;
}
