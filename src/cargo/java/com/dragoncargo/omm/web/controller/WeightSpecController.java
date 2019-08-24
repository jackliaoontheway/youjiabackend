package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.WeightSpec;
import com.dragoncargo.omm.model.service.WeightSpecService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/weightspecs")
public class WeightSpecController
        extends ModelController<WeightSpec, Integer, WeightSpecService>
{
    public WeightSpecController()
    {
        super(WeightSpec.class);
    }
}
