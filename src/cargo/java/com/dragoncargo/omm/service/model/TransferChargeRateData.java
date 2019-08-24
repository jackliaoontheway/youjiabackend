package com.dragoncargo.omm.service.model;

import lombok.Getter;
import lombok.Setter;

public class TransferChargeRateData extends BaseChargeRateData
{

    /**
     * 目的地机场
     */
    private @Setter @Getter String arrivingAirPortCode;

    private @Setter @Getter String arrivingAirPortName;

}
