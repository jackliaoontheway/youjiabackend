package com.dragoncargo.sales.service.model;

import com.dragoncargo.general.model.FeeInfo;
import com.polarj.model.GenericDbInfo;

import lombok.Getter;
import lombok.Setter;


public class BookingQuotationItemModify extends GenericDbInfo
{

    /**
     *
     */
    private static final long serialVersionUID = -3414028327821941815L;

    /**
     * 深圳机场报价 / 广州机场报价
     */
    private @Setter @Getter String airPortCode;

    /**
     * ChargeRateType 公开报价,最低报价,销售底价,公司成本
     */
    private @Setter @Getter String chargeRateType;

    /**
     * 报价项目编码
     */
    private @Setter @Getter String chargeName;

    /**
     * 计费单位, 重量 票 操作次数 个 页 天
     */

    private @Setter @Getter String chargeUnit;

    /**
     * 当选择 计费单位为 重量时 需要填 重量类型 毛重 计费重
     */
    private @Setter @Getter String weightType;

    /**
     * 计费单价
     */
    private @Setter @Getter FeeInfo unitPrice;

    /**
     * 最低收费
     */
    private @Setter @Getter FeeInfo minimumCharge;

    /**
     * 最高收费
     */
    private @Setter @Getter FeeInfo maximumCharge;
}
