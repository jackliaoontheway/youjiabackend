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
 * 发票信息
 */
@ModelMetaData(showDetailFieldName = "companyName")
@Entity
@Table(name = "invoiceinfo")
public class InvoiceInfo   extends GenericDbInfo {

    /**
     * 公司名称
     */
    @FieldMetaData(position = 10, label = "Company Name")
    @Column(length = 128)
    private @Getter @Setter String companyName;

    /**
     * 联系人
     */
    @FieldMetaData(position = 20, label = "Contacts")
    @Column(length = 128)
    private @Getter @Setter String contacts;

    /**
     * 注册地址
     */
    @FieldMetaData(position = 30, label = "Register Address")
    @Column(length = 200)
    private @Getter @Setter String registerAddress;

    /**
     * 收货地址
     */
    @FieldMetaData(position = 40, label = "Receive Address")
    @Column(length = 200)
    private @Getter @Setter String ReceiveAddress;

    /**
     * 联系人电话
     */
    @FieldMetaData(position = 50, label = "Phone")
    @Column(length = 32)
    private @Getter @Setter String phone;

    @FieldMetaData(position = 60, label = "Tax Num")
    @Column(length = 64)
    private @Getter @Setter String taxNum;

    /**
     * 银行
     */
    @FieldMetaData(position = 70, label = "Bank Name")
    @Column(length = 64)
    private @Getter @Setter String bankName;

    //银行账号
    @FieldMetaData(position = 80, label = "Bank Number")
    @Column(length = 64)
    private @Getter @Setter String bankNum;

}
