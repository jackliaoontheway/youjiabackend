package com.dragoncargo.omm.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dragoncargo.general.model.AviationAreaCode;
import com.dragoncargo.general.model.AviationCompany;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 运费附加费 燃油费 战险费
 */
@ModelMetaData
@Entity
@Table(name = "freightsubchargerate")
public @ToString @EqualsAndHashCode(callSuper = false) class FreightSubChargeRate extends AbstractChargeRate
{

    /**
     * 
     */
    private static final long serialVersionUID = 6020828970665034357L;

    /**
     * 航司
     */
    @FieldMetaData(position = 0, label = "Aviation Company", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AviationCompany.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "aviationCompanyId",
            foreignKey = @ForeignKey(name = "fk_freightsubchargerate_aviationcompany_aviationcompanyid"),
            referencedColumnName = "id")
    private @Setter @Getter AviationCompany aviationCompany;

    /**
     * 航线区域
     */
    @FieldMetaData(position = 10, label = "AviationArea Code", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AviationAreaCode.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "aviationAreaCodeId",
            foreignKey = @ForeignKey(name = "fk_freightsubchargerate_aviationareacode_aviationareacodeid"),
            referencedColumnName = "id")
    private @Setter @Getter AviationAreaCode aviationAreaCode;

}
