package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.Airline;
import com.dragoncargo.general.model.service.AirlineService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/airlines")
public class AirlineController
        extends ModelController<Airline, Integer, AirlineService>
{
    public AirlineController()
    {
        super(Airline.class);
    }
}
