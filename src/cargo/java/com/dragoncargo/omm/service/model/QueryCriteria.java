package com.dragoncargo.omm.service.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class QueryCriteria
{
    /**
     * 公开报价,最低报价,销售底价,公司成本
     */
    private @Setter @Getter List<String> chargeRateTypeList;

    /**
     * 起飞机场编码
     */
    private @Setter @Getter String departAirPortCode;

    private @Setter @Getter String arrivingAirPortCode;

    private @Setter @Getter String toCityCode;

    private @Setter @Getter String aviationCompanyCode;

    private @Setter @Getter WeightData weight;

    private @Setter @Getter DimensionsData dimensions;

    private @Setter @Getter VolumnData volumnData;

    private @Setter @Getter String packages;

    private @Setter @Getter String productName;

    private @Setter @Getter String pickupLocation;

    /**
     * 是否查询重量相近的等级报价
     */
    private @Setter @Getter boolean queryAdjacentRange;
    
    private @Setter @Getter String transferToCityCode;
}
