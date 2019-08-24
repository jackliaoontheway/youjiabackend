package com.dragoncargo.omm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dragoncargo.general.model.Airline;
import com.dragoncargo.general.model.AviationCompany;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 航班信息
 */
@ModelMetaData
@Entity
@Table(name = "flightinfo")
public @ToString @EqualsAndHashCode(callSuper = false) class FlightInfo extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -1155312921982846543L;

    /**
     * 航线
     */
    @FieldMetaData(position = 0, label = "Airline", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = Airline.class, labelField = "code")
    @ManyToOne
    @JoinColumn(name = "airlineId", foreignKey = @ForeignKey(name = "fk_flightinfo_airline_airlineId"),
            referencedColumnName = "id")
    private @Setter @Getter Airline airline;

    /**
     * 航司
     */
    @FieldMetaData(position = 10, label = "Aviation Company", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AviationCompany.class, labelField = "code")
    @ManyToOne
    @JoinColumn(name = "aviationcompanyId",
            foreignKey = @ForeignKey(name = "fk_flightinfo_aviationcompany_aviationcompanyid"),
            referencedColumnName = "id")
    private @Setter @Getter AviationCompany aviationCompany;

    /**
     * 航班号
     */
    @FieldMetaData(position = 20, label = "Flight Code", dataType = FieldMetaDataSupportedDataType.STRING,
            labelField = "name")
    @Column(length = 100)
    private @Setter @Getter String flightCode;

    /**
     * 起飞时间
     */
    @FieldMetaData(position = 50, label = "Depart Time", dataType = FieldMetaDataSupportedDataType.DATE,
            formatter = "yyyy-MM-dd hh:mm")
    @Column(length = 32)
    private @Setter @Getter Date departTime;

    /**
     * 落地时间
     */
    @FieldMetaData(position = 60, label = "Arriving Time", dataType = FieldMetaDataSupportedDataType.DATE,
            formatter = "yyyy-MM-dd hh:mm")
    @Column(length = 32)
    private @Setter @Getter Date arrivingTime;

    @FieldMetaData(position = 70, label = "Direct Flight", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Setter @Getter Boolean directFlight;

    /**
     * 班期
     */
    @FieldMetaData(position = 80, label = "schedule")
    @Column(length = 100)
    private @Setter @Getter String schedule;

}
