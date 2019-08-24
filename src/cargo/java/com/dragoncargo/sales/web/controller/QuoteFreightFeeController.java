package com.dragoncargo.sales.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.sales.model.QuoteFreightFee;
import com.dragoncargo.sales.model.service.QuoteFreightFeeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/quotefreightfees")
public class QuoteFreightFeeController
        extends ModelController<QuoteFreightFee, Integer, QuoteFreightFeeService>
{
    public QuoteFreightFeeController()
    {
        super(QuoteFreightFee.class);
    }
}
