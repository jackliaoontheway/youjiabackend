package com.dragoncargo.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@ModelMetaData(showDetailFieldName = "abbreviation")
@Entity
@Table(name = "customerinfo")
public class CustomerInfo extends GenericDbInfo {


    /**
     * 速记码
     */
    @FieldMetaData(position = 10, label = "shorthandCode",required = true)
    @Column(length = 64)
    private @Getter @Setter String shorthandCode;

    /**
     * 简称
     */
    @FieldMetaData(position = 20, label = "abbreviation",required = true)
    @Column(length = 64)
    private @Getter @Setter String abbreviation;

    /**
     * 全称
     */
    @FieldMetaData(position = 30, label = "fullName",required = true)
    @Column(length = 64)
    private @Getter @Setter String fullName;

    /**
     * fax
     */
    @FieldMetaData(position = 80, label = "Fax")
    @Column(length = 64)
    private @Getter @Setter String  fax;

    /**
     * 网址
     */
    @FieldMetaData(position = 90, label = "Website")
    @Column(length = 64)
    private @Getter @Setter String  website;

    /**
     * 邮箱
     */
    @FieldMetaData(position = 100, label = "Email")
    @Column(length = 64)
    private @Getter @Setter String email;


    /**
     * 地址
     */
    @FieldMetaData(position = 110, label = "Address Line")
    @Column(length = 200)
    private @Getter @Setter String addressLine;

    /**
     * 贸易性质
     */
    @FieldMetaData(position = 120, label = "Trade Nature")
    @Column(length = 64)
    private @Getter @Setter String tradeNature;

    /**
     * 客户类型 // 直客 or 代理
     */
    @FieldMetaData(position = 130, label = "Customer Type",enumClass = CustomerType.class,
            dataType = FieldMetaDataSupportedDataType.OBJECT,labelField = "name")
    @ManyToOne
    @JoinColumn(name = "customerTypeId", foreignKey = @ForeignKey(name = "fk_customerinfo_customerType_id"), referencedColumnName = "id")
    private @Getter @Setter CustomerType customerType;

    /**
     * 业务开始时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @FieldMetaData(position = 140, label = "Business Date", dataType = FieldMetaDataSupportedDataType.DATE,formatter="yyyy-MM-dd")
    @Column(length = 32)
    private @Getter @Setter Date businessDate;

    /**
     * 结算方式
     */
    @FieldMetaData(position = 150, label = "Settlement Method")
    @Column(length = 32)
    private @Getter @Setter String settlementMethod;

    /**
     * 账期类型
     */
    @FieldMetaData(position = 160, label = "Account Period Type")
    @Column(length = 32)
    private @Getter @Setter String accountPeriodType;

    /**
     * 结账日
     */
    @FieldMetaData(position = 170, label = "Bill Please Date")
    @Column(length = 32)
    private @Getter @Setter String billPleaseDate;

    /**
     * 结账天数
     */
    @FieldMetaData(position = 180, label = "Bill Please Day")
    @Column(length = 32)
    private @Getter @Setter Integer billPleaseDay;

    /**
     * 信用额度
     */
    @FieldMetaData(position = 190, label = "Credit line")
    @Column(length = 32)
    private @Getter @Setter String creditLine;

    /**
     * 是否fob客户?
     */
    @FieldMetaData(position = 200, label = "FOB", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column(length = 32)
    private @Getter @Setter boolean fob;

    /**
     * 是否电商客户
     */
    @FieldMetaData(position = 210, label = "EC Business", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column(length = 32)
    private @Getter @Setter boolean  ecBusiness;

    /**
     * 备注信息
     */
    @FieldMetaData(position = 220, label = "Memo")
    @Column(length = 1024)
    private @Getter @Setter String memo;
}
