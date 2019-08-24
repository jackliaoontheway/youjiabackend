package com.dragoncargo.omm.service.model.adapter;

import com.dragoncargo.general.model.AirportWarehouse;
import com.dragoncargo.omm.model.AirportWarehouseLocalChargeRate;
import com.dragoncargo.omm.service.model.AirportWarehouseLocalChargeRateData;

public class AirportWarehouseLocalChargeRateDataAdapter extends AirportWarehouseLocalChargeRateData
{

    public AirportWarehouseLocalChargeRateDataAdapter(AirportWarehouseLocalChargeRate chargeRateConfiguration)
    {
        AirportWarehouse airportWarehouse = chargeRateConfiguration.getAirportWarehouse();

        if (airportWarehouse != null)
        {
            this.setAirportWarehouseCode(airportWarehouse.getCode());
            this.setAirportWarehouseName(airportWarehouse.getName());
        }
    }

}
