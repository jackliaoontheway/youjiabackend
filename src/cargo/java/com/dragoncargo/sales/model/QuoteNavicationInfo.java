package com.dragoncargo.sales.model;

import com.dragoncargo.general.model.Airline;
import com.dragoncargo.general.model.AviationCompany;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@ModelMetaData
@Entity
@Table(name = "quotenavicationinfo")
public @ToString @EqualsAndHashCode(callSuper = false) class QuoteNavicationInfo extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -1155312921982846543L;

    /**
     * 航线
     */
    @FieldMetaData(position = 0, label = "Airline", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = Airline.class, labelField = "airlineCode")
    @ManyToOne
    @JoinColumn(name = "airlineId", foreignKey = @ForeignKey(name = "fk_quotenavicationinfo_airline_airlineId"),
            referencedColumnName = "id")
    private @Setter @Getter Airline airline;

    /**
     * 航司
     */
    @FieldMetaData(position = 10, label = "Aviation Company Code", dataType = FieldMetaDataSupportedDataType.STRING,
            enumClass = AviationCompany.class, labelField = "code")
    @Column(length = 100)
    private @Setter @Getter String aviationCompanyCode;

    @FieldMetaData(position = 40, label = "Direct Flight", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Setter @Getter Boolean directFlight;

    @FieldMetaData(position = 50, label = "Description")
    @Column(length = 2048)
    private @Setter @Getter String description;
}
