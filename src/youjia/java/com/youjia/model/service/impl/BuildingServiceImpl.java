package com.youjia.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Building;
import com.youjia.model.service.BuildingService;

@Service
public class BuildingServiceImpl extends EntityServiceImpl<Building, Integer>
        implements BuildingService
{
}
