package com.dragoncargo.omm.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 货站相关费用,理货费 物流园,国际货站
 */
public class AirportWarehouseLocalChargeRateData extends BaseChargeRateData
{
    /**
     *
     * 国际货站 International Cargo Centre Shenzhen（ICCS） 物流园 Logistics Park
     *
     */
    private @Setter @Getter String airportWarehouseCode;

    private @Setter @Getter String airportWarehouseName;
}
