package com.dragoncargo.omm.model;

import javax.persistence.Entity;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ModelMetaData
@Entity
public @ToString @EqualsAndHashCode(callSuper = false) class ULDGuaranteedType extends BaseCode
{
    /**
     * 
     */
    private static final long serialVersionUID = -602055781477802201L;

}
