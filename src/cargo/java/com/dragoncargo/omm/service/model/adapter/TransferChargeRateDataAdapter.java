package com.dragoncargo.omm.service.model.adapter;

import com.dragoncargo.omm.model.TransferChargeRate;
import com.dragoncargo.omm.service.model.TransferChargeRateData;

public class TransferChargeRateDataAdapter extends TransferChargeRateData
{

    public TransferChargeRateDataAdapter(TransferChargeRate configuration)
    {
        
        if(configuration != null && configuration.getArrivingAirPort() != null) {
            this.setArrivingAirPortCode(configuration.getArrivingAirPort().getCode());
            this.setArrivingAirPortName(configuration.getArrivingAirPort().getName());
        }
        
    }

}
