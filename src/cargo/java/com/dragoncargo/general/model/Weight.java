package com.dragoncargo.general.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.Getter;
import lombok.Setter;

@Embeddable
public class Weight
{

    /**
     * Kg/Lbs
     */
    @FieldMetaData(position = 10, label = "Unit", enumClass = WeightUnit.class,
            dataType = FieldMetaDataSupportedDataType.STRING)
    @Column(length = 16)
    @Enumerated(value = EnumType.STRING)
    private @Getter @Setter WeightUnit unit;

    @FieldMetaData(position = 20, label = "Value")
    @Column
    private @Getter @Setter BigDecimal value;

}
