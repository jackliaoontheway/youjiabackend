package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.ExchangeRate;
import com.dragoncargo.general.model.service.ExchangeRateService;

@Service
public class ExchangeRateServiceImpl extends EntityServiceImpl<ExchangeRate, Integer>
        implements ExchangeRateService
{
}
