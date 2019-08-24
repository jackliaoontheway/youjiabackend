package com.dragoncargo.customer.model;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import javax.persistence.Entity;

/**
 * 客户类别
 */
@Entity
@ModelMetaData(label = "CustomerCategory", showDetailFieldName = "code")
public class CustomerCategory extends BaseCode
{
//   ORDINARY_CUSTOMER,COMPANY_CUSTOMER
}
