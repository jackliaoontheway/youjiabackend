package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.WeightType;
import com.dragoncargo.general.model.service.WeightTypeService;

@Service
public class WeightTypeServiceImpl extends EntityServiceImpl<WeightType, Integer>
        implements WeightTypeService
{
}
