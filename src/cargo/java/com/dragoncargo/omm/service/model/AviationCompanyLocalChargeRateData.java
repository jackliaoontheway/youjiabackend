package com.dragoncargo.omm.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 航空公司相关的费用
 * 对应报价表里面的 附表1 + 附表2(燃油费 战险费)
 *
 */
public class AviationCompanyLocalChargeRateData extends BaseChargeRateData
{
    /**
     * 航司
     */
    private @Setter @Getter String aviationCompanyCode;
    
    private @Setter @Getter String aviationCompanyName;
}
