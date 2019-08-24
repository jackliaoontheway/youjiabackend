package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.InHouseLocalChargeRate;
import com.dragoncargo.omm.model.service.InHouseLocalChargeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/inhouselocalchargerates")
public class InHouseLocalChargeRateController
        extends ModelController<InHouseLocalChargeRate, Integer, InHouseLocalChargeRateService>
{
    public InHouseLocalChargeRateController()
    {
        super(InHouseLocalChargeRate.class);
    }
}
