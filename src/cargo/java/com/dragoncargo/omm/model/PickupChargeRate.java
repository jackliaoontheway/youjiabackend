package com.dragoncargo.omm.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "pickupchargerate")
public @ToString @EqualsAndHashCode(callSuper = false) class PickupChargeRate extends AbstractChargeRate
{

    /**
     * 
     */
    private static final long serialVersionUID = -3595692136070672754L;

    /**
     * 提货地点
     */
    @FieldMetaData(position = 0, label = "Pickup location", dataType = FieldMetaDataSupportedDataType.ARRAY,
            enumClass = PickupLocation.class, labelField = "name", multiChoice = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pickupchargerate_pickuplocation",
            joinColumns = @JoinColumn(name = "pickupfeeId",
                    foreignKey = @ForeignKey(name = "fk_pickupchargerate_pickuplocation_pickupfeeId")),
            inverseJoinColumns = @JoinColumn(name = "pickuplocationId",
                    foreignKey = @ForeignKey(name = "fk_pickupchargerate_pickuplocation_pickuplocationId")))
    private @Setter @Getter List<PickupLocation> pickupLocationList;

}
