package com.dragoncargo.general.model;

import javax.persistence.Entity;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 毛重 体积重 计费重
 *
 */
@ModelMetaData
@Entity
public @ToString @EqualsAndHashCode(callSuper = false) class WeightType extends BaseCode
{
    /**
     * 
     */
    private static final long serialVersionUID = 8188500078685610820L;

}
