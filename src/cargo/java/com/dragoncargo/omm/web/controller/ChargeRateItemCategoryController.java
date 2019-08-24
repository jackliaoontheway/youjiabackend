package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.ChargeRateItemCategory;
import com.dragoncargo.omm.model.service.ChargeRateItemCategoryService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/chargerateitemcategorys")
public class ChargeRateItemCategoryController
        extends ModelController<ChargeRateItemCategory, Integer, ChargeRateItemCategoryService>
{
    public ChargeRateItemCategoryController()
    {
        super(ChargeRateItemCategory.class);
    }
}
