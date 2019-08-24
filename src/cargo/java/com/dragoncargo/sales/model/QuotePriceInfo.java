package com.dragoncargo.sales.model;

import com.dragoncargo.general.model.FeeInfo;
import com.dragoncargo.general.model.Weight;
import com.dragoncargo.general.model.WeightType;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 费用项价格信息和类型
 */
@ModelMetaData
@Entity
@Table(name = "quotepriceinfo")
public @ToString @EqualsAndHashCode(callSuper = false) class QuotePriceInfo extends GenericDbInfo
{

    private static final long serialVersionUID = -7391609596113887596L;

    /**
     * 计费单价
     */
    @FieldMetaData(position = 30, label = "Unit Price", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${currency}${amount}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currency", column = @Column(name = "unitPriceCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "unitPriceValue")) })
    private @Setter @Getter FeeInfo unitPrice;

    /**
     * 当选择 计费单位为 重量时 需要填 重量类型 毛重 计费重
     */
    @FieldMetaData(position = 25, label = "Weight Type", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = WeightType.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "weightTypeId", foreignKey = @ForeignKey(name = "fk_quotePriceInfo_weighttypeid"),
            referencedColumnName = "id")
    private @Setter @Getter WeightType weightType;
}
