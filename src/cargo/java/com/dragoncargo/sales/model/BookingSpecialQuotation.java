package com.dragoncargo.sales.model;

import com.dragoncargo.general.model.ChargeUnit;
import com.dragoncargo.general.model.FeeInfo;
import com.dragoncargo.general.model.WeightType;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@ModelMetaData(label = "BookingSpecialQuotation",showDetailFieldName = "specialQuotation")
@Entity
@Table(name = "bookingspecialquotation")
public class BookingSpecialQuotation extends GenericDbInfo{

	/**
	 * 特殊优惠名称
	 */
	@FieldMetaData(position = 10, label = "Name")
	@Column(length = 64)
	private @Getter @Setter String name;

	/**
	 * 优惠项
	 */
	@FieldMetaData(position = 20, label = "Fee Items", dataType = FieldMetaDataSupportedDataType.STRING,
			enumClass = ChargeRateItem.class,labelField = "code", multiChoice = true)
	@Column(length = 1024)
	private @Getter @Setter String feeItems;

	/**
	 * 费用
	 */
	@FieldMetaData(position = 30, label = "Unit Price", dataType = FieldMetaDataSupportedDataType.OBJECT,
			embedded = true, formatter = "${currency}${amount}")
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "currency", column = @Column(name = "unitPriceCurrency")),
			@AttributeOverride(name = "amount", column = @Column(name = "unitPriceValue")) })
	private @Setter @Getter
	FeeInfo unitPrice;

	/**
	 * 计费单位, 重量(毛重，计费重) 票 操作次数 个 页 天
	 */
	@FieldMetaData(position = 40, label = "Charge Unit", dataType = FieldMetaDataSupportedDataType.OBJECT,
			enumClass = ChargeUnit.class, labelField = "name")
	@ManyToOne
	@JoinColumn(name = "chargeUnitId", foreignKey = @ForeignKey(name = "fk_BookingSpecialQuotation_chargeUnitid"),
			referencedColumnName = "id")
	private @Setter @Getter ChargeUnit chargeUnit;

	/**
	 * 当选择 计费单位为 重量时 需要填 重量类型 毛重 计费重
	 */
	@FieldMetaData(position = 50, label = "Weight Type", dataType = FieldMetaDataSupportedDataType.OBJECT,
			enumClass = WeightType.class, labelField = "name")
	@ManyToOne
	@JoinColumn(name = "weightTypeId", foreignKey = @ForeignKey(name = "fk_BookingSpecialQuotation_weighttypeid"),
			referencedColumnName = "id")
	private @Setter @Getter WeightType weightType;

	@FieldMetaData(position = 60, label = "Memo")
	@Column(length = 1024)
	private @Getter @Setter String memo;

}
