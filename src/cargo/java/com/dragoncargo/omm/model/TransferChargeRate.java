package com.dragoncargo.omm.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dragoncargo.general.model.AirPortInfo;
import com.dragoncargo.general.model.AviationCompany;
import com.dragoncargo.general.model.CityCode;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "transferchargerate")
public @ToString @EqualsAndHashCode(callSuper = false) class TransferChargeRate extends AbstractChargeRate
{

    /**
     * 
     */
    private static final long serialVersionUID = 8471143133683407219L;

    /**
     * 航司
     */
    @FieldMetaData(position = 30, label = "Aviation Company", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AviationCompany.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "aviationcompanyId",
            foreignKey = @ForeignKey(name = "fk_transferchargerate_aviationcompany_aviationcompanyid"),
            referencedColumnName = "id")
    private @Setter @Getter AviationCompany aviationCompany;

    /**
     * 落地机场 作为起始点
     */
    @FieldMetaData(position = 40, label = "Arriving AirPort", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AirPortInfo.class, labelField = "code")
    @ManyToOne
    @JoinColumn(name = "arrivingAirPortId",
            foreignKey = @ForeignKey(name = "fk_transferchargerate_arrivingairport_arrivingairportid"),
            referencedColumnName = "id")
    private @Setter @Getter AirPortInfo arrivingAirPort;

    /**
     * 转运方式 维护转运信息 班次
     */

    /**
     * 转运目的地点
     */
    @FieldMetaData(position = 120, label = "Transfer to location", dataType = FieldMetaDataSupportedDataType.ARRAY,
            enumClass = CityCode.class, labelField = "name")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "transferchargerate_transfertolocation",
            joinColumns = @JoinColumn(name = "transferchargerateId",
                    foreignKey = @ForeignKey(name = "fk_transferchargerate_transfertolocation_transferfeeId")),
            inverseJoinColumns = @JoinColumn(name = "cityId",
                    foreignKey = @ForeignKey(name = "fk_transferchargerate_transfertolocation_cityId")))
    private @Setter @Getter List<CityCode> transferToCityList;

}
