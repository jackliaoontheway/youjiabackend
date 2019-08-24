package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.AirportWarehouse;
import com.dragoncargo.general.model.service.AirportWarehouseService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/airportwarehouses")
public class AirportWarehouseController
        extends ModelController<AirportWarehouse, Integer, AirportWarehouseService>
{
    public AirportWarehouseController()
    {
        super(AirportWarehouse.class);
    }
}
