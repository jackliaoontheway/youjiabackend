package com.dragoncargo.omm.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "weightvolumnratespec")
public @ToString @EqualsAndHashCode(callSuper = false) class WeightVolumnRateSpec extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = 458822275233930781L;
    
    
    
    @FieldMetaData(position = 10, label = "code")
    @Column(length = 100)
    private @Setter @Getter String code;

    @FieldMetaData(position = 20, label = "Name")
    @Column(length = 100)
    private @Setter @Getter String name;
    
    /**
     * 100 : 1
     * 300 : 1
     * 500 : 1
     * 1000 : 1
     */
    @FieldMetaData(position = 30, label = "Rate")
    @Column
    private @Getter @Setter BigDecimal rate;

}
