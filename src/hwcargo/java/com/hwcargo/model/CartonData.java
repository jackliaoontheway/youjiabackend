package com.hwcargo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(searchField = "cartonName")
@Entity
@Table(name = "cartondata")
public @ToString @EqualsAndHashCode(callSuper = false) class CartonData extends GenericDbInfo
{

    /**
     * 
     */
    private static final long serialVersionUID = 7617791118191538773L;

    /**
     * 箱子序列号
     */
    @FieldMetaData(position = 0, label = "Carton Number", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter int cartonNo;

    /**
     * 箱子名称
     */
    @FieldMetaData(position = 10, label = "Box Name")
    @Column(length = 255)
    private @Setter @Getter String cartonName;

    /**
     * gw 毛重
     */
    @FieldMetaData(position = 20, label = "Gross Weight", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter double grossWeight;

    /**
     * 箱子的尺寸
     */
    @FieldMetaData(position = 30, label = "Carton Size")
    @Column(length = 1024)
    private @Setter @Getter String size;

    /**
     * volumn 体积
     */
    @FieldMetaData(position = 40, label = "Carton volumn", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter double volumn;

    /**
     * 箱单号
     */
    @FieldMetaData(position = 50, label = "Pl Number")
    @Column(length = 1024)
    private @Setter @Getter String plNo;

}
