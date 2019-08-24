package com.dragoncargo.omm.service.model;

import lombok.Getter;
import lombok.Setter;

public class FreightChargeRateData extends BaseChargeRateData
{

    /**
     * 航班信息
     */
    private @Setter @Getter FlightData flightData;

    private @Setter @Getter WeightSpecData weightSpecData;
    
    private @Setter @Getter boolean transferNeeded;
}
