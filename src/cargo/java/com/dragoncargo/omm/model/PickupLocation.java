package com.dragoncargo.omm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "pickuplocation")
// json化到前端时忽略hibernate生成的字段
@JsonIgnoreProperties("hibernateLazyInitializer")
public @ToString @EqualsAndHashCode(callSuper = false) class PickupLocation extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -2561741991045026250L;

    /**
     * 区域code 比如深圳 广州
     */
    @FieldMetaData(position = 0, label = "Pickup Area", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = PickupArea.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "pickupAreaId", foreignKey = @ForeignKey(name = "fk_pickuplocation_pickuparea_pickupareaid"),
            referencedColumnName = "id")
    private @Setter @Getter PickupArea pickupArea;

    @FieldMetaData(position = 10, label = "code")
    @Column(length = 100)
    private @Setter @Getter String code;

    @FieldMetaData(position = 20, label = "Name") 
    @Column(length = 100)
    private @Setter @Getter String name;

}
