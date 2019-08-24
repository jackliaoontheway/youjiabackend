package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.ChargeRateType;
import com.dragoncargo.omm.model.service.ChargeRateTypeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/chargeratetypes")
public class ChargeRateTypeController
        extends ModelController<ChargeRateType, Integer, ChargeRateTypeService>
{
    public ChargeRateTypeController()
    {
        super(ChargeRateType.class);
    }
}
