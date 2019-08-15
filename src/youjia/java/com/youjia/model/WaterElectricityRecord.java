package com.youjia.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "水电记录")
@Entity
@Table(name = "waterelectricityrecord")
public @ToString @EqualsAndHashCode(callSuper = false) class WaterElectricityRecord extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;

	@FieldMetaData(position = 0, label = "房间", dataType = FieldMetaDataSupportedDataType.OBJECT, required = true, enumClass = Room.class, labelField = "value")
	@ManyToOne
	@JoinColumn(name = "roomId", foreignKey = @ForeignKey(name = "fk_waterelectricityrecord_room_roomId"), referencedColumnName = "id")
	private @Getter @Setter Room room;

	@FieldMetaData(position = 10, label = "月份", dataType = FieldMetaDataSupportedDataType.DATE, formatter = FieldMetaDataSupportedDataType.DATEMONTHFORMAT)
	@Column(length = 32)
	private @Setter @Getter Date month;

	@FieldMetaData(position = 20, label = "冷水(吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "coldWaterTon")
	private @Setter @Getter Double coldWaterTon;

	@FieldMetaData(position = 30, label = "热水(吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "hotWaterTon")
	private @Setter @Getter Double hotWaterTon;

	@FieldMetaData(position = 40, label = "电(度)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "electricQuantity")
	private @Setter @Getter Double electricQuantity;

}
