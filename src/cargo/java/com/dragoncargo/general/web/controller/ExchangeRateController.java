package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.ExchangeRate;
import com.dragoncargo.general.model.service.ExchangeRateService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/exchangerates")
public class ExchangeRateController
        extends ModelController<ExchangeRate, Integer, ExchangeRateService>
{
    public ExchangeRateController()
    {
        super(ExchangeRate.class);
    }
}
