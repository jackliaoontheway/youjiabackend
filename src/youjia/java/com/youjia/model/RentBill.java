package com.youjia.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "月账单")
@Entity
@Table(name = "rentbill")
public @ToString @EqualsAndHashCode(callSuper = false) class RentBill extends GenericDbInfo {
	/**
	* 
	*/
	private static final long serialVersionUID = 4500084327771555452L;

	@FieldMetaData(position = 0, label = "租户", dataType = FieldMetaDataSupportedDataType.OBJECT, labelField = "name", required = true, enumClass = Renter.class)
	@ManyToOne
	@JoinColumn(name = "renterId", foreignKey = @ForeignKey(name = "fk_rentbill_renter_renterId"), referencedColumnName = "id")
	private @Getter @Setter Renter renter;

	@FieldMetaData(position = 5, label = "房间", dataType = FieldMetaDataSupportedDataType.OBJECT, required = true, enumClass = Room.class, labelField = "label")
	@OneToOne
	@JoinColumn(name = "roomId", foreignKey = @ForeignKey(name = "fk_rentbill_room_roomId"))
	private @Getter @Setter Room room;

	@FieldMetaData(position = 10, label = "月份", dataType = FieldMetaDataSupportedDataType.DATE, formatter = FieldMetaDataSupportedDataType.DATEMONTHFORMAT)
	@Column(length = 32)
	private @Setter @Getter Date effetiveDate;

	@FieldMetaData(position = 15, label = "是否已交租", required = true, enumClass = PayStatus.class)
	@Column(name = "payStatus")
	private @Setter @Getter String payStatus;

	@FieldMetaData(position = 20, label = "上月冷水(吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "lastMonthColdWaterTon")
	private @Setter @Getter Double lastMonthColdWaterTon;

	@FieldMetaData(position = 30, label = "上月热水(吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "lastMonthHotWaterTon")
	private @Setter @Getter Double lastMonthHotWaterTon;

	@FieldMetaData(position = 40, label = "上月电(度)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "lastMonthElectricQuantity")
	private @Setter @Getter Double lastMonthElectricQuantity;

	@FieldMetaData(position = 50, label = "本月冷水(吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "currentMonthColdWaterTon")
	private @Setter @Getter Double currentMonthColdWaterTon;

	@FieldMetaData(position = 60, label = "本月热水(吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "currentMonthHotWaterTon")
	private @Setter @Getter Double currentMonthHotWaterTon;

	@FieldMetaData(position = 70, label = "本月电(度)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "currentMonthElectricQuantity")
	private @Setter @Getter Double currentMonthElectricQuantity;

	@FieldMetaData(position = 80, label = "租金", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "rent")
	private @Setter @Getter Integer rent;

	@FieldMetaData(position = 90, label = "网费", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "netFee")
	private @Setter @Getter Integer netFee;

	@FieldMetaData(position = 100, label = "管理费", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "manageFee")
	private @Setter @Getter Integer manageFee;

	@FieldMetaData(position = 110, label = "冷水单价(每吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "coldWaterPrice")
	private @Setter @Getter Double coldWaterPrice;

	@FieldMetaData(position = 120, label = "热水单价(每吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "hotWaterPrice")
	private @Setter @Getter Double hotWaterPrice;

	@FieldMetaData(position = 130, label = "电费单价(每度)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "electricPrice")
	private @Setter @Getter Double electricPrice;

	@FieldMetaData(position = 110, label = "冷水金额", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "coldWaterFee")
	private @Setter @Getter Double coldWaterFee;

	@FieldMetaData(position = 120, label = "热水金额", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "hotWaterFee")
	private @Setter @Getter Double hotWaterFee;

	@FieldMetaData(position = 130, label = "电费金额", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "electricFee")
	private @Setter @Getter Double electricFee;

	@FieldMetaData(position = 140, label = "总计金额", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "totalFee")
	private @Setter @Getter Double totalFee;

}
