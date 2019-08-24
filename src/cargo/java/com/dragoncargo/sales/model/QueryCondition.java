package com.dragoncargo.sales.model;

import com.dragoncargo.customer.model.CustomerCenter;
import com.dragoncargo.general.model.*;
import com.dragoncargo.omm.model.PickupLocation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 询价条件
 */
@ModelMetaData(showDetailFieldName = "queryCondition")
@Entity
@Table(name = "querycondition")
public class QueryCondition extends GenericDbInfo {

    // 1.提货点联动(始发机场) 2.始发机场 3收货城市
    // 起始地
//    @FieldMetaData(position = 10, label = "Origin")
//    @Column
//    private @Setter @Getter String origin;
//
//    // 目的地
//    @FieldMetaData(position = 20, label = "Destination")
//    @Column
//    private @Setter @Getter String destination;

    @FieldMetaData(position = 10, label = "Depart Air Port", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AirPortInfo.class, labelField = "name",required = true)
    @ManyToOne
    @JoinColumn(name = "airlineId", foreignKey = @ForeignKey(name = "fk_queryCondition_departAirPortId"),
            referencedColumnName = "id")
    private @Setter @Getter AirPortInfo departAirPort;

    @FieldMetaData(position = 2, label = "To City", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = CityCode.class, labelField = "name",required = true)
    @ManyToOne
    @JoinColumn(name = "cityCodeId", foreignKey = @ForeignKey(name = "fk_queryCondition_toCityId"),
            referencedColumnName = "id")
    private @Setter @Getter CityCode toCity;

    // 件数
    @FieldMetaData(position = 30, label = "Quantity", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Integer quantity;

    // 毛重重量
    @FieldMetaData(position = 40, label = "Weight", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "weightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "weightValue")) })
    private @Setter @Getter Weight weight;

    @FieldMetaData(position = 40, label = "Volumn", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "volumeUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "volumeValue")) })
    private @Setter @Getter Volumn volume;

    // 品名
    @FieldMetaData(position = 60, label = "GoodsName")
    @Column
    private @Setter @Getter String goodsName;

    @FieldMetaData(position = 10, label = "PickupLocation", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = PickupLocation.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "pickupLocationId", foreignKey = @ForeignKey(name = "fk_queryCondition_pickupLocationId"),
            referencedColumnName = "id")
    private @Setter @Getter PickupLocation pickupLocation;

//    @FieldMetaData(position = 60, label = "PickupLocation")
//    @Column
//    private @Setter @Getter String pickupLocation;

    /**
     * 客户档案
     */
    @FieldMetaData(position = 150, label = "Customer Center", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = CustomerCenter.class,labelField = "customerCode",hasOwner = true,managementSeparately = true)
    @Transient
    private @Getter @Setter CustomerCenter customerCenter;

}
