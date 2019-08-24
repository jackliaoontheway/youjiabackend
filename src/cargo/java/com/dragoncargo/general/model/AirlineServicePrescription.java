package com.dragoncargo.general.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 航线时效
 */
@ModelMetaData(label = "AirlineServicePrescription", showDetailFieldName = "airlineCode")
@Entity
@Table(name = "airlineserviceprescription")
public @ToString @EqualsAndHashCode(callSuper = false) class AirlineServicePrescription extends GenericDbInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 171925788424405693L;

	 @FieldMetaData(position = 0, label = "Airline", dataType = FieldMetaDataSupportedDataType.OBJECT,
	            enumClass = Airline.class, labelField = "code")
	 @ManyToOne
	 @JoinColumn(name = "airlineId", foreignKey = @ForeignKey(name = "fk_airlineserviceprescription_airline_airlineId"),
			referencedColumnName = "id")
	 private @Setter @Getter Airline airline;

	@FieldMetaData(position = 90, label = "ServicePrescription")
	@Column(length = 100)
	private @Getter @Setter String servicePrescription;
}
