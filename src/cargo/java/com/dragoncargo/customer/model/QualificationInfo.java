package com.dragoncargo.customer.model;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.component.FileInfo;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 企业资质信息
 */
@ModelMetaData(showDetailFieldName = "qualificationInfo")
@Entity
@Table(name = "qualificationinfo")
public class QualificationInfo extends GenericDbInfo {


    @FieldMetaData(position = 10, label = "Code")
    @Column(length = 64)
    private @Getter @Setter String code;

    @FieldMetaData(position = 20, label = "Certificate", embedded = true, dataType = FieldMetaDataSupportedDataType.FILE)
    @Embedded
    private @Getter @Setter FileInfo certificate;

}
