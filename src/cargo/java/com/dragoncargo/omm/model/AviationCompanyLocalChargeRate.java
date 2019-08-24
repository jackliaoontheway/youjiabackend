package com.dragoncargo.omm.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dragoncargo.general.model.AviationCompany;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "aviationcompanylocalchargerate")
public @ToString @EqualsAndHashCode(callSuper = false) class AviationCompanyLocalChargeRate extends AbstractChargeRate
{

    /**
     * 
     */
    private static final long serialVersionUID = -437464581969110175L;

    /**
     * 航司
     */
    @FieldMetaData(position = 0, label = "Aviation Company", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AviationCompany.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "aviationcompanyId",
            foreignKey = @ForeignKey(name = "fk_aclocalchargerate_aviationcompany_aviationcompanyid"),
            referencedColumnName = "id")
    private @Setter @Getter AviationCompany aviationCompany;

}
