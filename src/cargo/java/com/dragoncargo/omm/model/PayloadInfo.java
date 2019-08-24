package com.dragoncargo.omm.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.dragoncargo.general.model.Weight;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仓位用量
 */
@Entity
@Table(name = "payloadinfo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "classification")
public @ToString @EqualsAndHashCode(callSuper = false) class PayloadInfo extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -2175504290988773688L;

    @FieldMetaData(position = 0, label = "Maximum weight payload", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "maximumWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "maximumWeightValue")) })
    private @Setter @Getter Weight maximumWeight;

    @FieldMetaData(position = 10, label = "Available weight payload", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "availableWeightUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "availableWeightValue")) })
    private @Setter @Getter Weight availableWeight;

    @FieldMetaData(position = 20, label = "Maximum volumn payload", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "maximumVolumnUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "maximumVolumnValue")) })
    private @Setter @Getter Weight maximumVolumn;

    @FieldMetaData(position = 30, label = "Available volumn payload", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${value}${unit}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "unit", column = @Column(name = "availableVolumnUnit")),
            @AttributeOverride(name = "value", column = @Column(name = "availableVolumnValue")) })
    private @Setter @Getter Weight availableVolumn;
}
