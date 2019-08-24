package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.Airline;
import com.dragoncargo.general.model.service.AirlineService;

@Service
public class AirlineServiceImpl extends EntityServiceImpl<Airline, Integer>
        implements AirlineService
{
}
