package com.dragoncargo.omm.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
public @ToString @EqualsAndHashCode(callSuper = false) class FlightPayload extends PayloadInfo
{

    /**
     * 
     */
    private static final long serialVersionUID = -2552539728528013769L;

    @FieldMetaData(position = 0, label = "Flight Info", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = FlightInfo.class, labelField = "flightCode")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flightInfoId", foreignKey = @ForeignKey(name = "fk_payloadinfo_flightinfo_id"))
    private @Getter @Setter FlightInfo flightInfo;

}
