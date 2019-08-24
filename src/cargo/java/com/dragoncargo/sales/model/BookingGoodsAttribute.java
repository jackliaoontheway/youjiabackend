package com.dragoncargo.sales.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dragoncargo.general.model.Volumn;
import com.dragoncargo.general.model.Weight;
import com.dragoncargo.omm.model.WeightSpec;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.Getter;
import lombok.Setter;

@ModelMetaData(label = "GoodsAttribute", showDetailFieldName = "weight")
@Entity
@Table(name = "bookinggoodsattribute")
public class BookingGoodsAttribute extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -267645140540175921L;

    /**
     * 品名 用,分隔
     */
    @FieldMetaData(position = 10, label = "goodsName")
    @Column(length = 64)
    private @Setter @Getter String goodsName;

    /**
     * 外包装的数量
     */
    @FieldMetaData(position = 20, label = "Quantity", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column(length = 32)
    private @Setter @Getter Integer quantity;

    /**
     * 重量
     */
    @FieldMetaData(position = 30, label = "Weight", dataType = FieldMetaDataSupportedDataType.OBJECT, embedded = true,
            formatter = "${unit}${value}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "weightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "weightValue")) })
    private @Setter @Getter Weight weight;

    /**
     * 等级重量
     */
    @FieldMetaData(position = 40, label = "Weight Spec", dataType = FieldMetaDataSupportedDataType.OBJECT,
            labelField = "name", enumClass = WeightSpec.class, required = true)
    @ManyToOne
    @JoinColumn(name = "weightspecId", foreignKey = @ForeignKey(name = "fk_bookingorder_weightspec_id"),
            referencedColumnName = "id")
    private @Setter @Getter WeightSpec weightSpec;

    /**
     * 分泡比率 50%= 0.5 60%=0.6
     */
    @FieldMetaData(position = 50, label = "BulkyCargo Discount Rate", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Double bulkyCargoDiscountRate;

    /**
     * 体积
     */
    @FieldMetaData(position = 50, label = "Volumn", dataType = FieldMetaDataSupportedDataType.OBJECT, embedded = true,
            formatter = "${unit}${value}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "volumeUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "volumeValue")) })
    private @Setter @Getter Volumn volume;
}
