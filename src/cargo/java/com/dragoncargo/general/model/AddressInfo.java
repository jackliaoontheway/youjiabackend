package com.dragoncargo.general.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "addressinfo")
public @ToString @EqualsAndHashCode(callSuper = false) class AddressInfo extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -5667721027458172308L;

    @FieldMetaData(position = 0, label = "StreetLines1")
    @Column(length = 1024)
    public @Setter @Getter String streetLines1;

    @FieldMetaData(position = 10, label = "StreetLines2")
    @Column(length = 1024)
    public @Setter @Getter String streetLines2;

    @FieldMetaData(position = 20, label = "City")
    @Column(length = 255)
    public @Setter @Getter String city;

    @FieldMetaData(position = 30, label = "State Or Province Code")
    @Column(length = 255)
    public @Setter @Getter String stateOrProvinceCode;

    @FieldMetaData(position = 40, label = "Postal Code")
    @Column(length = 255)
    public @Setter @Getter String postalCode;

    @FieldMetaData(position = 50, label = "Country Code")
    @Column(length = 255)
    public @Setter @Getter String countryCode;

    @FieldMetaData(position = 60, label = "Country Name")
    @Column(length = 255)
    public @Setter @Getter String countryName;
}
