package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.FreightChargeRate;
import com.dragoncargo.omm.model.service.FreightChargeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/freightchargerates")
public class FreightChargeRateController
        extends ModelController<FreightChargeRate, Integer, FreightChargeRateService>
{
    public FreightChargeRateController()
    {
        super(FreightChargeRate.class);
    }
}
