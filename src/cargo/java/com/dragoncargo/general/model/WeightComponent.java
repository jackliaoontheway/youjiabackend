package com.dragoncargo.general.model;

import lombok.Getter;
import lombok.Setter;

public class WeightComponent
{

    // 毛重
    private @Setter @Getter Weight grossWeight;

    // 体积重
    private @Setter @Getter Weight volumeWeight;

    // 计费重 体积重和毛重的最大值
    private @Setter @Getter Weight actualWeight;

    // 收费重 计费重分泡后得出的重量
    private @Setter @Getter Weight chargeWeight;

    // 付费重 供应商的计重
    private @Setter @Getter Weight paymentWeight;

}
