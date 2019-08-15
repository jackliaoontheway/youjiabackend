package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.WaterElectricityCharge;
import com.youjia.model.service.WaterElectricityChargeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/waterelectricitycharges")
public class WaterElectricityChargeController
        extends ModelController<WaterElectricityCharge, Integer, WaterElectricityChargeService>
{
    public WaterElectricityChargeController()
    {
        super(WaterElectricityCharge.class);
    }
}
