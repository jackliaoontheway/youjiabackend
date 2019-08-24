package com.dragoncargo.general.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
 * 航空公司
 *
 */
@ModelMetaData
@Entity
@Table(name = "aviationcompany")
public @ToString @EqualsAndHashCode(callSuper = false) class AviationCompany extends GenericDbInfo
{

    /**
     * 
     */
    private static final long serialVersionUID = 3487927607157279099L;

    @I18nKeyField
    @FieldMetaData(position = 0, label = "Code")
    @Column(length = 64)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 10, label = "Name")
    @Column(length = 255)
    private @Setter @Getter String name;

    @FieldMetaData(position = 30, label = "Address", dataType = FieldMetaDataSupportedDataType.OBJECT,
            labelField = "code")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressinfoId", foreignKey = @ForeignKey(name = "fk_aviationcompany_addressinfo_addressinfoid"),
            referencedColumnName = "id")
    private @Setter @Getter AddressInfo addressInfo;

}
