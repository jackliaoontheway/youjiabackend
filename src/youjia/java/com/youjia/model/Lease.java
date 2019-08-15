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
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "租约管理")
@Entity
@Table(name = "lease")
public @ToString @EqualsAndHashCode(callSuper = false) class Lease extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;

	@FieldMetaData(position = 0, label = "租户", dataType = FieldMetaDataSupportedDataType.OBJECT, labelField = "name", required = true, enumClass = Renter.class)
	@ManyToOne
	@JoinColumn(name = "renterId", foreignKey = @ForeignKey(name = "fk_lease_renter_renterId"), referencedColumnName = "id")
	private @Getter @Setter Renter renter;

	@FieldMetaData(position = 5, label = "房间", dataType = FieldMetaDataSupportedDataType.OBJECT, required = true, enumClass = Room.class, labelField = "roomNo")
	@OneToOne
	@JoinColumn(name = "roomId", foreignKey = @ForeignKey(name = "fk_lease_room_roomId"))
	private @Getter @Setter Room room;

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

	@FieldMetaData(position = 50, label = "网费", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
	@Column(name = "netFee")
	private @Setter @Getter boolean netFee;

	@FieldMetaData(position = 60, label = "管理费", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "manageFee")
	private @Setter @Getter Integer manageFee;

}
