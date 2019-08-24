package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.ChargeUnit;
import com.dragoncargo.general.model.service.ChargeUnitService;

@Service
public class ChargeUnitServiceImpl extends EntityServiceImpl<ChargeUnit, Integer>
        implements ChargeUnitService
{
}
