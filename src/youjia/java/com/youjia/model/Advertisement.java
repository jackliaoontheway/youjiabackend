package com.youjia.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.component.PersonalPhoto;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "广告设置")
@Entity
@Table(name = "advertisement")
public @ToString @EqualsAndHashCode(callSuper = false) class Advertisement extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;
	
	@FieldMetaData(position = 10, label = "广告描述")
	@Column(name = "description")
	private @Setter @Getter String description;

    @FieldMetaData(position = 20, label = "广告图片", embedded = true, dataType = FieldMetaDataSupportedDataType.FILE)
    @Embedded
    private @Getter @Setter PersonalPhoto photo;

}
