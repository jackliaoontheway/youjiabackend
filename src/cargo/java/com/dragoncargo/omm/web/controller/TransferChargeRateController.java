package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.TransferChargeRate;
import com.dragoncargo.omm.model.service.TransferChargeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/transferchargerates")
public class TransferChargeRateController
        extends ModelController<TransferChargeRate, Integer, TransferChargeRateService>
{
    public TransferChargeRateController()
    {
        super(TransferChargeRate.class);
    }
}
