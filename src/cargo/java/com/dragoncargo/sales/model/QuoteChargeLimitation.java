package com.dragoncargo.sales.model;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ModelMetaData(label = " Quote Charge limitation", showDetailFieldName = "description")
@Entity
@Table(name = "quotechargelimitation")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "classification")
public @ToString @EqualsAndHashCode(callSuper = false) class QuoteChargeLimitation extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -4932026487832361938L;
    @FieldMetaData(position = 0, label = "Description")
    @Column(length = 2048)
    private @Getter @Setter String description;

}
