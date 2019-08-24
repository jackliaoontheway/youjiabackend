package com.dragoncargo.sales.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.sales.model.QuotePriceInfo;
import com.dragoncargo.sales.model.service.QuotePriceInfoService;

@Service
public class QuotePriceInfoServiceImpl extends EntityServiceImpl<QuotePriceInfo, Integer>
        implements QuotePriceInfoService
{
}
