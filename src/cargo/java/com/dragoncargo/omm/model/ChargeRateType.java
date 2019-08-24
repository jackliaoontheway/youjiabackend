package com.dragoncargo.omm.model;

import javax.persistence.Entity;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ModelMetaData
@Entity
public @ToString @EqualsAndHashCode(callSuper = false) class ChargeRateType extends BaseCode
{
    /**
     * 
     */
    private static final long serialVersionUID = 3636700620144238306L;

    // RATE_FOR_PUBLIC, RATE_FOR_MINIMAL, RATE_FOR_SALES, RATE_FOR_COMPANY

}
