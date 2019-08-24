package com.dragoncargo.omm.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
 * 报价项目归类
 * 
 * 运费,运费附加费,机场本地费,机场仓库本地费,航司本地费,提货费,转运费
 */
@ModelMetaData
@Entity
@Table(name = "chargerateitemcategory")
public @ToString @EqualsAndHashCode(callSuper = false) class ChargeRateItemCategory extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -9156857630836757640L;

    @I18nKeyField
    @FieldMetaData(position = 10, label = "Code")
    @Column(length = 64)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 20, label = "Name")
    @Column(length = 64)
    private @Setter @Getter String name;

    @FieldMetaData(position = 30, label = "Description")
    @Column(length = 200)
    private @Setter @Getter String description;

    @FieldMetaData(position = 40, label = "Simple Query", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Setter @Getter Boolean simpleQueryAvailable;

    @FieldMetaData(position = 50, label = "Multiple Query", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Setter @Getter Boolean multipleQueryAvailable;

    @Column(length = 255)
    private @Setter @Getter String chargeRateQueryServiceClassFullName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chargeRateCategory")
    private @Getter @Setter List<ChargeRateItem> chargeRateItemList;
}
