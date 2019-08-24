package com.dragoncargo.sales.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.sales.model.QuoteChargeItem;
import com.dragoncargo.sales.model.service.QuoteChargeItemService;

@Service
public class QuoteChargeItemServiceImpl extends EntityServiceImpl<QuoteChargeItem, Integer>
        implements QuoteChargeItemService
{
}
