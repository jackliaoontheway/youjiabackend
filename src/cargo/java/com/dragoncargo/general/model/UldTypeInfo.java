package com.dragoncargo.general.model;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//拼板ULD规格
@ModelMetaData(label = "UldTypeinfo", showDetailFieldName = "uldType")
@Entity
@Table(name = "uldtypeinfo")
public class UldTypeInfo extends GenericDbInfo {

	@FieldMetaData(position = 10, label = "Uld Type")
	@Column(length = 64)
	private @Getter @Setter String uldType;
	
	//应用类型
	@FieldMetaData(position = 20, label = "Apply Type")
	@Column(length = 64)
	private @Getter @Setter String applyType;
	
	//集装类
	@FieldMetaData(position = 30, label = "Container Type")
	@Column(length = 64)
	private @Getter @Setter String containerType;

	//自重
	@FieldMetaData(position = 40, label = "SelfWeight")
	@Column(length = 64)
	private @Getter @Setter Double selfWeight;
	
	//可装载重量
	@FieldMetaData(position = 50, label = "EnableLoad Weight")
	@Column(length = 64)
	private @Getter @Setter Double enableLoadWeight;

	//适用机型
	@FieldMetaData(position = 60, label = "Airline")
	@Column(length = 100)
	private @Getter @Setter String suitPlaneType;

}
