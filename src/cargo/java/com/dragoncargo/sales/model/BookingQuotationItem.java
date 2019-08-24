package com.dragoncargo.sales.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dragoncargo.general.model.FeeInfo;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.Getter;
import lombok.Setter;

@ModelMetaData(label = "BookingQuotationItem", showDetailFieldName = "chargeName")
@Entity
@Table(name = "bookingquotationitem")
public class BookingQuotationItem extends GenericDbInfo
{

    /**
     * 
     */
    private static final long serialVersionUID = -3414028327821941815L;

    /**
     * 深圳机场报价 / 广州机场报价
     */
    @FieldMetaData(position = 0, label = "AirPort Code")
    private @Setter @Getter String airPortCode;

    /**
     * ChargeRateType 公开报价,最低报价,销售底价,公司成本
     */
    @FieldMetaData(position = 5, label = "Charge Rate Type")
    private @Setter @Getter String chargeRateType;

    /**
     * 报价项目编码
     */
    @FieldMetaData(position = 10, label = "ChargeRate Code")
    @Column(length = 100)
    private @Setter @Getter String chargeName;

    /**
     * 计费单位, 重量 票 操作次数 个 页 天
     */
    @FieldMetaData(position = 20, label = "Charge Unit")
    @Column(length = 100)
    private @Setter @Getter String chargeUnit;

    /**
     * 当选择 计费单位为 重量时 需要填 重量类型 毛重 计费重
     */
    @FieldMetaData(position = 25, label = "Weight Type")
    private @Setter @Getter String weightType;

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
     * 最低收费
     */
    @FieldMetaData(position = 40, label = "Minimum Charge", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${currency}${amount}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currency", column = @Column(name = "minimumChargeCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimumChargeValue")) })
    private @Setter @Getter FeeInfo minimumCharge;

    /**
     * 最高收费
     */
    @FieldMetaData(position = 50, label = "Maximum Charge", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${currency}${amount}", hide = true)
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currency", column = @Column(name = "maximumChargeCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "maximumChargeValue")) })
    private @Setter @Getter FeeInfo maximumCharge;
}
