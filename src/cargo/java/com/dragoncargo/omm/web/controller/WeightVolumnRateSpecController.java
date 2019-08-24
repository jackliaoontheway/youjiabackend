package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.WeightVolumnRateSpec;
import com.dragoncargo.omm.model.service.WeightVolumnRateSpecService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/weightvolumnratespecs")
public class WeightVolumnRateSpecController
        extends ModelController<WeightVolumnRateSpec, Integer, WeightVolumnRateSpecService>
{
    public WeightVolumnRateSpecController()
    {
        super(WeightVolumnRateSpec.class);
    }
}
