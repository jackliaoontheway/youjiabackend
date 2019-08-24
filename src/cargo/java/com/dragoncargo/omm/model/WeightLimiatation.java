package com.dragoncargo.omm.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dragoncargo.general.model.Weight;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.Getter;
import lombok.Setter;

@ModelMetaData
@Entity
@Table(name = "weightlimiatation")
public class WeightLimiatation extends ChargeRateLimitation
{

    /**
     * 
     */
    private static final long serialVersionUID = 8943403638730191740L;

    @FieldMetaData(position = 0, label = "Maximum weight payload", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "maximumWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "maximumWeightValue")) })
    private @Setter @Getter Weight maximumWeight;

}
