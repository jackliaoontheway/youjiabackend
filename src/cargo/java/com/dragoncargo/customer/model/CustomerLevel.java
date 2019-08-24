package com.dragoncargo.customer.model;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import javax.persistence.Entity;

/**
 * 客户级别
 */
@Entity
@ModelMetaData(label = "CustomerLevel", showDetailFieldName = "code")
public class CustomerLevel extends BaseCode
{
//   VIP,ORDINART
}
