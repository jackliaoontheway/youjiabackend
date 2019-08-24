package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.AirportWarehouseLocalChargeRate;
import com.dragoncargo.omm.model.service.AirportWarehouseLocalChargeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/airportwarehouselocalchargerates")
public class AirportWarehouseLocalChargeRateController
        extends ModelController<AirportWarehouseLocalChargeRate, Integer, AirportWarehouseLocalChargeRateService>
{
    public AirportWarehouseLocalChargeRateController()
    {
        super(AirportWarehouseLocalChargeRate.class);
    }
}
