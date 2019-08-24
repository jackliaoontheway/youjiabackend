package com.dragoncargo.omm.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "freightchargerate")
public @ToString @EqualsAndHashCode(callSuper = false) class FreightChargeRate extends AbstractChargeRate
{

    /**
     * 
     */
    private static final long serialVersionUID = 3405830669845600623L;

    /**
     * 重量规格
     */
    @FieldMetaData(position = 150, label = "Weight Spec", dataType = FieldMetaDataSupportedDataType.OBJECT,
            labelField = "name", enumClass = WeightSpec.class, required = true)
    @ManyToOne
    @JoinColumn(name = "weightspecId", foreignKey = @ForeignKey(name = "fk_freightchargerate_weightspec_id"),
            referencedColumnName = "id")
    private @Setter @Getter WeightSpec weightSpec;

    /**
     * 重积比规格
     */
    @FieldMetaData(position = 160, label = "Weight Volmn Rate Spec", dataType = FieldMetaDataSupportedDataType.OBJECT,
            labelField = "name", enumClass = WeightVolumnRateSpec.class)
    @ManyToOne
    @JoinColumn(name = "weightvolumnratespecId",
            foreignKey = @ForeignKey(name = "fk_freightchargerate_weightvolumnratespec_id"), referencedColumnName = "id")
    private @Setter @Getter WeightVolumnRateSpec weightVolumnRateSpec;

    /**
     * 包板，包仓，散销 包量
     */
    @FieldMetaData(position = 170, label = "ULD Guaranteed Type", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = ULDGuaranteedType.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "uldguaranteedtypeId",
            foreignKey = @ForeignKey(name = "fk_freightchargerate_uldguaranteedtype_uldguaranteedtypeid"),
            referencedColumnName = "id")
    private @Setter @Getter ULDGuaranteedType uldGuaranteedType;

    /**
     * 航运信息
     */
    @FieldMetaData(position = 180, label = "Navication Info", dataType = FieldMetaDataSupportedDataType.OBJECT,
            labelField = "name", enumClass = NavicationInfo.class, required = true)
    @ManyToOne
    @JoinColumn(name = "navicationInfoId", foreignKey = @ForeignKey(name = "fk_freightchargerate_navicationInfo_id"),
            referencedColumnName = "id")
    private @Setter @Getter NavicationInfo navicationInfo;
    
    /**
     * 是否需要转运
     */
    @Transient
    private @Setter @Getter boolean transferNeeded;
}
