package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.AirPortInfo;
import com.dragoncargo.general.model.service.AirPortInfoService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/airportinfos")
public class AirPortInfoController
        extends ModelController<AirPortInfo, Integer, AirPortInfoService>
{
    public AirPortInfoController()
    {
        super(AirPortInfo.class);
    }
}
