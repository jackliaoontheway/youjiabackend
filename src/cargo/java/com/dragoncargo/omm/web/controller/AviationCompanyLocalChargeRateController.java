package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.AviationCompanyLocalChargeRate;
import com.dragoncargo.omm.model.service.AviationCompanyLocalChargeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/aviationcompanylocalchargerates")
public class AviationCompanyLocalChargeRateController
        extends ModelController<AviationCompanyLocalChargeRate, Integer, AviationCompanyLocalChargeRateService>
{
    public AviationCompanyLocalChargeRateController()
    {
        super(AviationCompanyLocalChargeRate.class);
    }
}
