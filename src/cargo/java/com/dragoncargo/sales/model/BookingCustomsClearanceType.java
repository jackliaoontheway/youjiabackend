package com.dragoncargo.sales.model;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import javax.persistence.Entity;

@Entity
@ModelMetaData(label = "BookingCustomsClearanceType", showDetailFieldName = "code")
public class BookingCustomsClearanceType extends BaseCode
{

    /**
     *
     */
    private static final long serialVersionUID = 2630253123194708319L;

}
