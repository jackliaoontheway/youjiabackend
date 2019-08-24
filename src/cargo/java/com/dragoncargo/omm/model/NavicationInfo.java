package com.dragoncargo.omm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dragoncargo.general.model.Airline;
import com.dragoncargo.general.model.AviationAreaCode;
import com.dragoncargo.general.model.AviationCompany;
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
 * 航运信息
 *
 */
@ModelMetaData
@Entity
@Table(name = "navicationinfo")
public @ToString @EqualsAndHashCode(callSuper = false) class NavicationInfo extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -1155312921982846543L;

    /**
     * 航线区域 作为一个航线分组的标致 跟航空公司相关 不同的航空公司分组会有差别
     */
    @FieldMetaData(position = 15, label = "AviationArea Code", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AviationAreaCode.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "aviationAreaCodeId",
            foreignKey = @ForeignKey(name = "fk_navicationinfo_aviationareacode_aviationareacodeid"),
            referencedColumnName = "id")
    private @Setter @Getter AviationAreaCode aviationAreaCode;

    /**
     * 航线 + 航司
     */
    @I18nKeyField
    @FieldMetaData(position = 0, label = "Code")
    @Column(length = 64)
    private @Setter @Getter String code;

    @I18nField
    @FieldMetaData(position = 10, label = "Name")
    @Column(length = 255)
    private @Setter @Getter String name;

    /**
     * 航线
     */
    @FieldMetaData(position = 20, label = "Airline", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = Airline.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "airlineId", foreignKey = @ForeignKey(name = "fk_navicationinfo_airline_airlineId"),
            referencedColumnName = "id")
    private @Setter @Getter Airline airline;

    /**
     * 航司
     */
    @FieldMetaData(position = 30, label = "Aviation Company", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AviationCompany.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "aviationcompanyId",
            foreignKey = @ForeignKey(name = "fk_navicationinfo_aviationcompany_aviationcompanyid"),
            referencedColumnName = "id")
    private @Setter @Getter AviationCompany aviationCompany;

    @FieldMetaData(position = 40, label = "Direct Flight", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Setter @Getter Boolean directFlight;

    @FieldMetaData(position = 50, label = "Description")
    @Column(length = 2048)
    private @Setter @Getter String description;
}
