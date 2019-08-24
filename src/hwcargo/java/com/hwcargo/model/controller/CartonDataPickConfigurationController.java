package com.hwcargo.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hwcargo.model.CartonDataPickConfiguration;
import com.hwcargo.model.service.CartonDataPickConfigurationService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/cartondatapickconfigurations")
public class CartonDataPickConfigurationController
        extends ModelController<CartonDataPickConfiguration, Integer, CartonDataPickConfigurationService>
{
    public CartonDataPickConfigurationController()
    {
        super(CartonDataPickConfiguration.class);
    }
}
