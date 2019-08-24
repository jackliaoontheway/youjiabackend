package com.dragoncargo.omm.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "airportlocalchargerate")
public @ToString @EqualsAndHashCode(callSuper = false) class AirportLocalChargeRate extends AbstractChargeRate
{
    /**
     * 
     */
    private static final long serialVersionUID = -1238551370015609303L;

}
