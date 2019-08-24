package com.dragoncargo.omm.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.omm.model.ChargeRateItem;
import com.dragoncargo.omm.model.service.ChargeRateItemService;

@Service
public class ChargeRateItemServiceImpl extends EntityServiceImpl<ChargeRateItem, Integer>
        implements ChargeRateItemService
{
}
