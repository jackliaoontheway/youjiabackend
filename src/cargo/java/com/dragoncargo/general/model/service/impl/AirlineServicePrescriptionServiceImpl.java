package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.AirlineServicePrescription;
import com.dragoncargo.general.model.service.AirlineServicePrescriptionService;

@Service
public class AirlineServicePrescriptionServiceImpl extends EntityServiceImpl<AirlineServicePrescription, Integer>
        implements AirlineServicePrescriptionService
{
}
