package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.ChargeUnit;
import com.dragoncargo.general.model.service.ChargeUnitService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/chargeunits")
public class ChargeUnitController
        extends ModelController<ChargeUnit, Integer, ChargeUnitService>
{
    public ChargeUnitController()
    {
        super(ChargeUnit.class);
    }
}
