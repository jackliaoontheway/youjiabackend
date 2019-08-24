package com.dragoncargo.sales.model;

import com.dragoncargo.general.model.Weight;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@ModelMetaData
@Entity
@Table(name = "quoteweightcomponent")
public class QuoteWeightComponent extends GenericDbInfo
{

    // 毛重
    @FieldMetaData(position = 10, label = "Weight", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${unit}${value}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "grossWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "grossWeightValue")) })
    private @Setter @Getter Weight grossWeight;

    // 体积重
    @FieldMetaData(position = 10, label = "Weight", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${unit}${value}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "volumeWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "volumeWeightValue")) })
    private @Setter @Getter Weight volumeWeight;

    // 计费重 体积重和毛重的最大值
    @FieldMetaData(position = 10, label = "Weight", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${unit}${value}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "actualWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "actualWeightValue")) })
    private @Setter @Getter Weight actualWeight;

    // 收费重 计费重分泡后得出的重量
    @FieldMetaData(position = 10, label = "Weight", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${unit}${value}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "chargeWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "chargeWeightValue")) })
    private @Setter @Getter Weight chargeWeight;

    // 付费重 供应商的计重
    @FieldMetaData(position = 10, label = "Weight", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${unit}${value}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "paymentWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "paymentWeightValue")) })
    private @Setter @Getter Weight paymentWeight;



}
