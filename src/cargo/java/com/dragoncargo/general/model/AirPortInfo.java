package com.dragoncargo.general.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 机场信息管理
 *
 */
@ModelMetaData(label = "AirportInfo", showDetailFieldName = "code")
@Entity
@Table(name = "airportinfo")
// json化到前端时忽略hibernate生成的字段
@JsonIgnoreProperties("hibernateLazyInitializer")
public @ToString @EqualsAndHashCode(callSuper = false) class AirPortInfo extends GenericDbInfo
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 四字编码
     */
    @I18nKeyField
    @FieldMetaData(position = 0, label = "Code")
    @Column(length = 64)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 10, label = "Name")
    @Column(length = 255)
    private @Setter @Getter String name;

    @FieldMetaData(position = 20, label = "domesticAndInternational")
    @Column(length = 100)
    private @Getter @Setter String domesticAndInternational;

    @FieldMetaData(position = 30, label = "Address", dataType = FieldMetaDataSupportedDataType.OBJECT,
            labelField = "code")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressinfoId", foreignKey = @ForeignKey(name = "fk_airportinfo_addressinfo_addressinfoid"),
            referencedColumnName = "id")
    private @Setter @Getter AddressInfo addressInfo;
}
