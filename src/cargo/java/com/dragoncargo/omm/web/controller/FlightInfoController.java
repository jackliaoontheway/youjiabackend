package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.FlightInfo;
import com.dragoncargo.omm.model.service.FlightInfoService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/flightinfos")
public class FlightInfoController
        extends ModelController<FlightInfo, Integer, FlightInfoService>
{
    public FlightInfoController()
    {
        super(FlightInfo.class);
    }
}
