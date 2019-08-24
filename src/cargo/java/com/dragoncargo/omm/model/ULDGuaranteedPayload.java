package com.dragoncargo.omm.model;

import javax.persistence.Entity;

import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ModelMetaData
@Entity
public @ToString @EqualsAndHashCode(callSuper = false) class ULDGuaranteedPayload extends PayloadInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -2128158359418434942L;

}
