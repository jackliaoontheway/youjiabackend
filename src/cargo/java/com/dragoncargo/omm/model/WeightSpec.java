package com.dragoncargo.omm.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dragoncargo.general.model.Weight;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "weightspec")
public @ToString @EqualsAndHashCode(callSuper = false) class WeightSpec extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = 3504221456329898017L;

    /**
     * 45+ 100+ 300+ 500+ 1000+ 2000+ 3000+
     */
    @FieldMetaData(position = 10, label = "code")
    @Column(length = 100)
    private @Setter @Getter String code;

    @FieldMetaData(position = 20, label = "Name")
    @Column(length = 100)
    private @Setter @Getter String name;

    @FieldMetaData(position = 30, label = "Weight Rank")
    @Column
    private @Setter @Getter Integer weightRank;

    /**
     * 
     * Minimum <= value < Maximum
     */
    @FieldMetaData(position = 130, label = "Minimum Range Weight", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "minimumRangeWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "minimumRangeWeightValue")) })
    private @Setter @Getter Weight minimumRangeWeight;

    @FieldMetaData(position = 140, label = "Maximum Range Weight", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "maximumRangeWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "maximumRangeWeightValue")) })
    private @Setter @Getter Weight maximumRangeWeight;

}
