package com.youjia.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "租约管理", tabField = "leaseStatus", tabValues = "生效;申请退租;已退租;到期")
@Entity
@Table(name = "lease")
public @ToString @EqualsAndHashCode(callSuper = false) class Lease extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;

	@FieldMetaData(position = 0, label = "租户", dataType = FieldMetaDataSupportedDataType.OBJECT, managementSeparately = true, labelField = "name", required = true, enumClass = Renter.class)
	@ManyToOne
	@JoinColumn(name = "renterId", foreignKey = @ForeignKey(name = "fk_lease_renter_renterId"), referencedColumnName = "id")
	private @Getter @Setter Renter renter;

	@FieldMetaData(position = 5, label = "房间", dataType = FieldMetaDataSupportedDataType.OBJECT, managementSeparately = true, labelField = "label", required = true, enumClass = Room.class)
	@OneToOne
	@JoinColumn(name = "roomId", foreignKey = @ForeignKey(name = "fk_lease_room_roomId"))
	private @Getter @Setter Room room;

	@I18nField
	@FieldMetaData(position = 8, label = "状态", required = true, enumClass = LeaseStatus.class)
	@Column(name = "leaseStatus")
	private @Setter @Getter String leaseStatus;

	@FieldMetaData(position = 10, label = "租约开始时间", dataType = FieldMetaDataSupportedDataType.DATE, formatter = FieldMetaDataSupportedDataType.DATEFORMAT)
	@Column(length = 32)
	private @Setter @Getter Date effetiveDate;

	@FieldMetaData(position = 20, label = "租约到期时间", dataType = FieldMetaDataSupportedDataType.DATE, formatter = FieldMetaDataSupportedDataType.DATEFORMAT)
	@Column(length = 32)
	private @Setter @Getter Date expiredDate;

	@FieldMetaData(position = 30, label = "租金", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "rent")
	private @Setter @Getter Integer rent;

	@FieldMetaData(position = 40, label = "押金", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "deposit")
	private @Setter @Getter Integer deposit;

	@FieldMetaData(position = 50, label = "网费", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "netFee")
	private @Setter @Getter Integer netFee;

	@FieldMetaData(position = 60, label = "管理费", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "manageFee")
	private @Setter @Getter Integer manageFee;

	@Transient
	private @Setter @Getter String buildingNumber;

	@Transient
	private @Setter @Getter String effetiveDateString;

	@Transient
	private @Setter @Getter String expiredDateString;

}
