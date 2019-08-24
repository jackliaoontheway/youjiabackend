package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.FreightSubChargeRate;
import com.dragoncargo.omm.model.service.FreightSubChargeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/freightsubchargerates")
public class FreightSubChargeRateController
        extends ModelController<FreightSubChargeRate, Integer, FreightSubChargeRateService>
{
    public FreightSubChargeRateController()
    {
        super(FreightSubChargeRate.class);
    }
}
