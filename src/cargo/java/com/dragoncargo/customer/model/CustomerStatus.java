package com.dragoncargo.customer.model;

import com.dragoncargo.general.model.BaseCode;
import com.polarj.model.annotation.ModelMetaData;

import javax.persistence.Entity;

/**
 * 客户状态
 */
@Entity
@ModelMetaData(label = "CustomerStatus", showDetailFieldName = "code")
public class CustomerStatus extends BaseCode
{
//   在用,停用
}
