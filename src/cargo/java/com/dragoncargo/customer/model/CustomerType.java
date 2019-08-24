package com.dragoncargo.customer.model;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import javax.persistence.Entity;

/**
 * 客户类型
 */
@Entity
@ModelMetaData(label = "CustomerType", showDetailFieldName = "code")
public class CustomerType extends BaseCode
{
//   AGENCY,DIRECT
}
