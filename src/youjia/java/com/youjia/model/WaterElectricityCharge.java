package com.youjia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "水电费设置")
@Entity
@Table(name = "waterelectricitycharge")
public @ToString @EqualsAndHashCode(callSuper = false) class WaterElectricityCharge extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;

	@FieldMetaData(position = 10, label = "冷水单价(每吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "coldWaterPrice")
	private @Setter @Getter Double coldWaterPrice;

	@FieldMetaData(position = 20, label = "热水单价(每吨)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "hotWaterPrice")
	private @Setter @Getter Double hotWaterPrice;

	@FieldMetaData(position = 30, label = "电费单价(每度)", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "electricPrice")
	private @Setter @Getter Double electricPrice;

}
