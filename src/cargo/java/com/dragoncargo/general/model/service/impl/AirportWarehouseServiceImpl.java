package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.AirportWarehouse;
import com.dragoncargo.general.model.service.AirportWarehouseService;

@Service
public class AirportWarehouseServiceImpl extends EntityServiceImpl<AirportWarehouse, Integer>
        implements AirportWarehouseService
{
}
