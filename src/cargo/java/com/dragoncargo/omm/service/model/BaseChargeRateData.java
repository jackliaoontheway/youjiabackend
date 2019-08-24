package com.dragoncargo.omm.service.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public abstract class BaseChargeRateData
{

    /**
     * 机场编码 名称
     */
    private @Setter @Getter String airPortCode;
    private @Setter @Getter String airPortName;
    
    /**
     * ChargeRateType 公开报价,最低报价,销售底价,公司成本
     */
    private @Setter @Getter String chargeRateTypeCode;
    private @Setter @Getter String chargeRateTypeName;

    /**
     * 计费项编码 名称
     */
    private @Setter @Getter String chargeRateCode;
    private @Setter @Getter String chargeRateName;

    /**
     * 计费单位编码 KG/LBS 票 操作次数 个 页 天
     */
    private @Setter @Getter String chargeUnitCode;
    private @Setter @Getter String chargeUnitName;

    /**
     * 重量类型code 毛重 计费重
     */
    private @Setter @Getter String weightTypeCode;
    private @Setter @Getter String weightTypeName;

    /**
     * 计费单价
     */
    private @Setter @Getter FeeData unitPrice;

    /**
     * 最低收费
     */
    private @Setter @Getter FeeData minimumCharge;

    /**
     * 最高收费
     */
    private @Setter @Getter FeeData maximumCharge;

    /**
     * 生效日期
     */
    private @Setter @Getter Date effetiveDate;

    /**
     * 失效日期
     */
    private @Setter @Getter Date expiredDate;

    private @Setter @Getter String remark;
}
