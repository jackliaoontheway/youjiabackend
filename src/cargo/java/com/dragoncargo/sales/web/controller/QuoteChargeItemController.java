package com.dragoncargo.sales.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.sales.model.QuoteChargeItem;
import com.dragoncargo.sales.model.service.QuoteChargeItemService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/quotechargeitems")
public class QuoteChargeItemController
        extends ModelController<QuoteChargeItem, Integer, QuoteChargeItemService>
{
    public QuoteChargeItemController()
    {
        super(QuoteChargeItem.class);
    }
}
