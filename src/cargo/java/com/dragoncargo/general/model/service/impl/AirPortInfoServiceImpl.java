package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.AirPortInfo;
import com.dragoncargo.general.model.service.AirPortInfoService;

@Service
public class AirPortInfoServiceImpl extends EntityServiceImpl<AirPortInfo, Integer>
        implements AirPortInfoService
{
}
