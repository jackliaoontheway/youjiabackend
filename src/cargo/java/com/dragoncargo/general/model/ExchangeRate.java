package com.dragoncargo.general.model;

import java.math.BigDecimal;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//汇率信息管理
@ModelMetaData(label = "ExchangeRate", showDetailFieldName = "original")
@Entity
@Table(name = "exchangerate")
public class ExchangeRate extends GenericDbInfo {
	
	//汇率生效时间
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm")
	@FieldMetaData(position = 10, label = "timeStamp", dataType = FieldMetaDataSupportedDataType.DATE,
			formatter = "yyyy-MM-dd hh:mm")
	@Column(length = 32)
	private @Getter @Setter Date effetiveDate;

	//原始币种
	@FieldMetaData(position = 20, label = "Original")
	@Column(length = 64)
	private @Getter @Setter String original;

	//目标币种
	@FieldMetaData(position = 30, label = "Target")
	@Column(length = 64)
	private @Getter @Setter String target;

	//原始币种值 可以不用
	@FieldMetaData(position = 40, label = "OriginalValue", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(length = 64)
	private @Getter @Setter BigDecimal originalValue;

	
	//目标币种值 可以不用
	@FieldMetaData(position = 50, label = "TargetValue", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(length = 64)
	private @Getter @Setter BigDecimal targetValue;
	
	//target/orgin
	@FieldMetaData(position = 60, label = "Rate", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(length = 64)
	private @Getter @Setter Double rate;

}
