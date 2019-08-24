package com.dragoncargo.general.model;

import javax.persistence.Entity;

import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ModelMetaData
@Entity
public @ToString @EqualsAndHashCode(callSuper = false) class ChargeUnit extends BaseCode
{
    /**
     * 
     */
    private static final long serialVersionUID = 5237655896916003403L;

}
