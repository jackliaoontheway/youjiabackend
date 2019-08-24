package com.dragoncargo.omm.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "inhouselocalchargerate")
public @ToString @EqualsAndHashCode(callSuper = false) class InHouseLocalChargeRate extends AbstractChargeRate
{
    /**
     * 
     */
    private static final long serialVersionUID = -7027955550565248993L;

}
