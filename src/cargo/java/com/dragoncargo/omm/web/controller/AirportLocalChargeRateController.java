package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.AirportLocalChargeRate;
import com.dragoncargo.omm.model.service.AirportLocalChargeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/airportlocalchargerates")
public class AirportLocalChargeRateController
        extends ModelController<AirportLocalChargeRate, Integer, AirportLocalChargeRateService>
{
    public AirportLocalChargeRateController()
    {
        super(AirportLocalChargeRate.class);
    }
}
