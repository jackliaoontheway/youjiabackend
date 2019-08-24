package com.dragoncargo.customer.model;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@ModelMetaData(showDetailFieldName = "contactsName")
@Entity
@Table(name = "customerconcat")
public class CustomerConcat extends GenericDbInfo {

    /**
     * 业务联系人
     */
    @FieldMetaData(position = 10, label = "Contacts Name")
    @Column(length = 64)
    private @Getter @Setter String contactsName;

    /**
     * 业务联系人电话
     */
    @FieldMetaData(position = 20, label = "Contacts Phone")
    @Column(length = 64)
    private @Getter @Setter String contactsPhone;

    /**
     * 联系人类型 业务联系人/财务联系人
     */
    @FieldMetaData(position = 30, label = "Contacts Type",enumClass = ConcatsType.class,
            dataType = FieldMetaDataSupportedDataType.STRING,labelField = "name")
    @Column(length = 64)
    private @Getter @Setter String contactsType;
}
