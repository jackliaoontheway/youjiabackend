package com.dragoncargo.general.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.Getter;
import lombok.Setter;

@Embeddable
public class Volumn
{

    /**
     * cm/m 立方米 立方厘米
     */
    @FieldMetaData(position = 10, label = "Unit", enumClass = DimensionsUnit.class,
            dataType = FieldMetaDataSupportedDataType.STRING)
    @Column(length = 16)
    private @Getter @Setter String unit;

    @FieldMetaData(position = 20, label = "Value")
    @Column
    private @Getter @Setter BigDecimal value;

}
