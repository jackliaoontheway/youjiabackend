package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.WeightType;
import com.dragoncargo.general.model.service.WeightTypeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/weighttypes")
public class WeightTypeController
        extends ModelController<WeightType, Integer, WeightTypeService>
{
    public WeightTypeController()
    {
        super(WeightType.class);
    }
}
