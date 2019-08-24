package com.polarj.common.web.controller;

import com.polarj.model.ModelFilterStrategy;
import com.polarj.model.service.ModelFilterStrategyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modelfilterstrategies")
public class ModelFilterStrategyController extends ModelController<ModelFilterStrategy, Integer, ModelFilterStrategyService>
{

    public ModelFilterStrategyController()
    {
        super(ModelFilterStrategy.class);
    }
}
