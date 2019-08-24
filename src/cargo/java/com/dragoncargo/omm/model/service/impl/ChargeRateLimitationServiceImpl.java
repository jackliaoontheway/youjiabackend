package com.dragoncargo.omm.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.omm.model.ChargeRateLimitation;
import com.dragoncargo.omm.model.service.ChargeRateLimitationService;

@Service
public class ChargeRateLimitationServiceImpl extends EntityServiceImpl<ChargeRateLimitation, Integer>
        implements ChargeRateLimitationService
{
}
