package com.dragoncargo.omm.service.model;

import lombok.Getter;
import lombok.Setter;

public class QueryChargeRateComponentResponse
{

    private @Setter @Getter QueryChargeRateResponse<FreightChargeRateData> freightChargeRateDataResponse;

    private @Setter @Getter QueryChargeRateResponse<AirportLocalChargeRateData> airportLocalChargeRateDataResponse;

    private @Setter @Getter QueryChargeRateResponse<
            AirportWarehouseLocalChargeRateData> airportWarehouseLocalChargeRateDataResponse;

    private @Setter @Getter QueryChargeRateResponse<
            AviationCompanyLocalChargeRateData> aviationCompanyLocalChargeRateDataResponse;

    private @Setter @Getter QueryChargeRateResponse<TransferChargeRateData> transferChargeRateDataResponse;

    private @Setter @Getter QueryChargeRateResponse<PickupChargeRateData> pickupChargeRateDataResponse;

    private @Setter @Getter QueryChargeRateResponse<FreightSubChargeRateData> freightSubChargeRateDataResponse;

    private @Setter @Getter QueryChargeRateResponse<InHouseLocalChargeRateData> inHouseLocalChargeRateDataResponse;

}
