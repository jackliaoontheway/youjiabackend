package com.dragoncargo.omm.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.omm.model.ChargeRateType;
import com.dragoncargo.omm.model.service.ChargeRateTypeService;

@Service
public class ChargeRateTypeServiceImpl extends EntityServiceImpl<ChargeRateType, Integer>
        implements ChargeRateTypeService
{
}
