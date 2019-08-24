package com.dragoncargo.general.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 *
 * 国际货站 International Cargo Centre Shenzhen（ICCS） 物流园 Logistics Park
 *
 */
@ModelMetaData
@Entity
@Table(name = "airportwarehouse")
public @ToString @EqualsAndHashCode(callSuper = false) class AirportWarehouse extends GenericDbInfo
{

    /**
     * 
     */
    private static final long serialVersionUID = -6292267980095056094L;

    @I18nKeyField
    @FieldMetaData(position = 0, label = "Code")
    @Column(length = 64)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 10, label = "Name")
    @Column(length = 255)
    private @Setter @Getter String name;

    @FieldMetaData(position = 10, label = "AirPort", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AirPortInfo.class, labelField = "code")
    @ManyToOne
    @JoinColumn(name = "airPortInfoId",
            foreignKey = @ForeignKey(name = "fk_airportwarehouse_arrivingairport_airportinfoid"),
            referencedColumnName = "id")
    private @Setter @Getter AirPortInfo airPortInfo;

}
