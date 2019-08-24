package com.dragoncargo.omm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.Getter;
import lombok.Setter;

@ModelMetaData
@Entity
@Table(name = "electriclimitation")
public class ElectricLimitation extends ChargeRateLimitation
{

    /**
     * 
     */
    private static final long serialVersionUID = 3819958778180338211L;
    
    @FieldMetaData(position = 10, label = "Electric Allowed", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Boolean electricAllowed;
}
