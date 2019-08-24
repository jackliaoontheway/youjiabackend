package com.dragoncargo.omm.service.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public abstract class BaseCriteria
{

    /**
     * 公开报价,最低报价,销售底价,公司成本
     */
    private @Setter @Getter List<String> chargeRateTypeList;

    private @Setter @Getter String departAirPortCode;
    
    // 落地机场到转运目的地城市
    private @Setter @Getter String toCityCode;// 转运目的地城市
}
