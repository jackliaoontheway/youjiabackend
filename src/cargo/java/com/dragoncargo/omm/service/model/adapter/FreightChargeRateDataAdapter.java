package com.dragoncargo.omm.service.model.adapter;

import com.dragoncargo.omm.model.FreightChargeRate;
import com.dragoncargo.omm.service.model.FlightData;
import com.dragoncargo.omm.service.model.FreightChargeRateData;
import com.dragoncargo.omm.service.model.WeightSpecData;

public class FreightChargeRateDataAdapter extends FreightChargeRateData
{

    public FreightChargeRateDataAdapter(FreightChargeRate freightChargeRate)
    {
        FlightData flightData = new FlightData(freightChargeRate.getNavicationInfo());

        this.setFlightData(flightData);

        WeightSpecData weightSpecData = new WeightSpecData(freightChargeRate.getWeightSpec());

        this.setWeightSpecData(weightSpecData);
        
        this.setTransferNeeded(freightChargeRate.isTransferNeeded());
    }
}
