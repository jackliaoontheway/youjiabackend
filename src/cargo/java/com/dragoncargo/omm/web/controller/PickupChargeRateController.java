package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.PickupChargeRate;
import com.dragoncargo.omm.model.service.PickupChargeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/pickupchargerates")
public class PickupChargeRateController
        extends ModelController<PickupChargeRate, Integer, PickupChargeRateService>
{
    public PickupChargeRateController()
    {
        super(PickupChargeRate.class);
    }
}
