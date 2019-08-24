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
 * 合同信息
 */
@ModelMetaData(showDetailFieldName = "name")
@Entity
@Table(name = "agreementinfo")
public class AgreementInfo  extends GenericDbInfo {

    @FieldMetaData(position = 10, label = "Name")
    @Column(length = 64)
    private @Getter @Setter String name;

    @FieldMetaData(position = 20, label = "Description")
    @Column(length = 200)
    private @Getter @Setter String description;

    @FieldMetaData(position = 30, label = "Agreement", embedded = true, dataType = FieldMetaDataSupportedDataType.FILE)
    @Embedded
    private @Getter @Setter FileInfo agreement;
}
