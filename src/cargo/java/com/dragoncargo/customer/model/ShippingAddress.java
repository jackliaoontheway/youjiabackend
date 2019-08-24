package com.dragoncargo.customer.model;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 发货地信息
 */
@ModelMetaData(showDetailFieldName = "shippingAddress")
@Entity
@Table(name = "shippingaddress")
public class ShippingAddress extends GenericDbInfo {


    @FieldMetaData(position = 10, label = "Settlement Company")
    @Column(length = 64)
    private @Getter @Setter String settlementCompany;

    @FieldMetaData(position = 20, label = "Address Line")
    @Column(length = 200)
    private @Getter @Setter String addressLine;

}
