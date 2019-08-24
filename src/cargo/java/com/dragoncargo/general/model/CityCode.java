package com.dragoncargo.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarj.model.annotation.ModelMetaData;

import javax.persistence.Entity;

/**
 * 城市代码
 */
@ModelMetaData
@Entity
// json化到前端时忽略hibernate生成的字段
@JsonIgnoreProperties("hibernateLazyInitializer")
public class CityCode extends BaseCode
{

    /**
     * 
     */
    private static final long serialVersionUID = 6719464863609166882L;

}
