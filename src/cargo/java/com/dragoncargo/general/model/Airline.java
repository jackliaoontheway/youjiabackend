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
 * 做为报价的航线 直飞 转飞
 */
@ModelMetaData(label = "Airline", showDetailFieldName = "airlineCode")
@Entity
@Table(name = "airline")
public @ToString @EqualsAndHashCode(callSuper = false) class Airline extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = 368397156104811123L;

    @I18nKeyField
    @FieldMetaData(position = 0, label = "Code")
    @Column(length = 64)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 10, label = "Name")
    @Column(length = 255)
    private @Setter @Getter String name;

    /**
     * 始发地机场
     */
    @FieldMetaData(position = 20, label = "Depart AirPort", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AirPortInfo.class, labelField = "code")
    @ManyToOne
    @JoinColumn(name = "departAirPortId", foreignKey = @ForeignKey(name = "fk_airline_departairport_departairportid"),
            referencedColumnName = "id")
    private @Setter @Getter AirPortInfo departAirPort;

    /**
     * 转飞地机场
     */
    @FieldMetaData(position = 30, label = "Transit AirPort", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AirPortInfo.class, labelField = "code")
    @ManyToOne
    @JoinColumn(name = "transitAirPortId",
            foreignKey = @ForeignKey(name = "fk_airline_transitairport_transitairportid"), referencedColumnName = "id")
    private @Setter @Getter AirPortInfo transitAirPort;

    /**
     * 目的地机场
     */
    @FieldMetaData(position = 40, label = "Arriving AirPort", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AirPortInfo.class, labelField = "code")
    @ManyToOne
    @JoinColumn(name = "arrivingAirPortId",
            foreignKey = @ForeignKey(name = "fk_airline_arrivingairport_arrivingairportid"),
            referencedColumnName = "id")
    private @Setter @Getter AirPortInfo arrivingAirPort;

}
