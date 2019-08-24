package com.dragoncargo.omm.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "Charge limitation", showDetailFieldName = "description")
@Entity
@Table(name = "chargeratelimitation")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "classification")
public @ToString @EqualsAndHashCode(callSuper = false) class ChargeRateLimitation extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -4932026487832361938L;
    @FieldMetaData(position = 0, label = "Description")
    @Column(length = 2048)
    private @Getter @Setter String description;

}
