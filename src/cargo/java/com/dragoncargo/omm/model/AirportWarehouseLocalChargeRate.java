package com.dragoncargo.omm.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dragoncargo.general.model.AirportWarehouse;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "airportwarehouselocalchargerate")
public @ToString @EqualsAndHashCode(callSuper = false) class AirportWarehouseLocalChargeRate extends AbstractChargeRate
{
    /**
     * 
     */
    private static final long serialVersionUID = 6300866358393958754L;

    /**
     *
     * 国际货站 International Cargo Centre Shenzhen（ICCS） 物流园 Logistics Park
     *
     */
    @FieldMetaData(position = 0, label = "Airport Warehouse", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AirportWarehouse.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "airportWarehouseId",
            foreignKey = @ForeignKey(name = "fk_awlocalchargerate_airportwarehouse_airportwarehouseid"),
            referencedColumnName = "id")
    private @Setter @Getter AirportWarehouse airportWarehouse;

}
