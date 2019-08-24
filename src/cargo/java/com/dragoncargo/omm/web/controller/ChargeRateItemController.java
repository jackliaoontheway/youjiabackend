package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.service.ChargeRateItemService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/chargerateitems")
public class ChargeRateItemController
        extends ModelController<ChargeRateItem, Integer, ChargeRateItemService>
{
    public ChargeRateItemController()
    {
        super(ChargeRateItem.class);
    }
}
