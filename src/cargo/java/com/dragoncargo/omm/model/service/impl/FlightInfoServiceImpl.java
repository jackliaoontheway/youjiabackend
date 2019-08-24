package com.dragoncargo.omm.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.omm.model.FlightInfo;
import com.dragoncargo.omm.model.service.FlightInfoService;

@Service
public class FlightInfoServiceImpl extends EntityServiceImpl<FlightInfo, Integer>
        implements FlightInfoService
{
}
