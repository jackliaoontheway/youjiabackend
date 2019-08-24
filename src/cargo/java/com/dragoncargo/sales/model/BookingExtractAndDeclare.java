package com.dragoncargo.sales.model;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import javax.persistence.Entity;

@Entity
@ModelMetaData(label = "BookingExtractAndDeclare", showDetailFieldName = "code")
public class BookingExtractAndDeclare extends BaseCode {

}
