package com.dragoncargo.customer.model;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.component.PersonalPhoto;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

// 地址信息
@ModelMetaData(showDetailFieldName = "name")
@Entity
@Table(name = "customeraddressinfo")
public @ToString @EqualsAndHashCode(callSuper = false) class CustomerAddressInfo extends GenericDbInfo
{

    private static final long serialVersionUID = 8818530417335969130L;

    /**
     * 公司
     */
    @FieldMetaData(position = 10, label = "Name")
    @Column(length = 128)
    private @Getter @Setter String name;

    /**
     * 公司
     */
    @FieldMetaData(position = 20, label = "Company Name")
    @Column(length = 128)
    private @Getter @Setter String companyName;

    /**
     * 联系人的身份证号码或者其他证件号码
     */
    @FieldMetaData(position = 25, label = "ID Number")
    @Column(length = 64)
    private @Getter @Setter String idNumber;

    /**
     * 联系人电话
     */
    @FieldMetaData(position = 30, label = "Phone")
    @Column(length = 32)
    private @Getter @Setter String phone;

    /**
     * 该地址是否正确(用于地址校验的返回值，null: 状态不明，true：地址正确，
     * false：地址不正确，其他信息是推荐的更正信息)
     * QUES：这样做是否合适？是否需要改成枚举类型，增加非存储的国际化支持？
     */
    @Transient
    private @Getter @Setter Boolean valid;

    /**
     * 是否居民地址
     */
    @FieldMetaData(position = 40, label = "Residential Address", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Getter @Setter Boolean residential;

    /**
     * 邮箱
     */
    @FieldMetaData(position = 50, label = "Email")
    @Column(length = 64)
    private @Getter @Setter String email;

    /**
     * 一般是到路名与门牌号
     */
    @FieldMetaData(position = 60, label = "Address Line1")
    @Column(length = 64)
    private @Getter @Setter String addressLine1;

    /**
     * 一般是单元号（Fedex的限制35）
     */
    @FieldMetaData(position = 70, label = "Address Line2")
    @Column(length = 64)
    private @Getter @Setter String addressLine2;

    /**
     * 邮政编码（Fedex的限制35）
      */
    @FieldMetaData(position = 80, label = "Postal Code")
    @Column(length = 16)
    private @Getter @Setter String postalCode;

    /**
     * 城市
     */
    @FieldMetaData(position = 90, label = "City")
    @Column(length = 128)
    private @Getter @Setter String city;

    /**
     * 省，州，区等国家下一级单位，代码，国际标准：ISO 3166-2:[ISO 3166-1的两位代码]
     */
    @FieldMetaData(position = 100, label = "State Code")
    @Column(length = 16)
    private @Getter @Setter String state;

    /**
     * 省，州，区等国家下一级单位，代码，国际标准：ISO 3166-2:[ISO 3166-1的两位代码]
     */
    @FieldMetaData(position = 110, label = "State Name")
    @Column(length = 64)
    private @Getter @Setter String stateFullName;

    /**
     * 国家代码，国际标准：ISO 3166-1
     */
    @FieldMetaData(position = 120, label = "Country Code")
    @Column(length = 16)
    private @Getter @Setter String country;

    /**
     * 国家代码，国际标准：ISO 3166-1
     */
    @FieldMetaData(position = 130, label = "Country Name")
    @Column(length = 128)
    private @Getter @Setter String countryFullName;

    @FieldMetaData(position = 140, label = "Photo", embedded = true, dataType = FieldMetaDataSupportedDataType.FILE)
    @Embedded
    private @Getter @Setter PersonalPhoto photo;

}
