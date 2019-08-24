package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.AirlineServicePrescription;
import com.dragoncargo.general.model.service.AirlineServicePrescriptionService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/airlineserviceprescriptions")
public class AirlineServicePrescriptionController
        extends ModelController<AirlineServicePrescription, Integer, AirlineServicePrescriptionService>
{
    public AirlineServicePrescriptionController()
    {
        super(AirlineServicePrescription.class);
    }
}
