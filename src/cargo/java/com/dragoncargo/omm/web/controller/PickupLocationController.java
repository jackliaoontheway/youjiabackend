package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.PickupLocation;
import com.dragoncargo.omm.model.service.PickupLocationService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/pickuplocations")
public class PickupLocationController
        extends ModelController<PickupLocation, Integer, PickupLocationService>
{
    public PickupLocationController()
    {
        super(PickupLocation.class);
    }
}
