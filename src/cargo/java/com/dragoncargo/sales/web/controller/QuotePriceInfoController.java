package com.dragoncargo.sales.web.controller;

import com.dragoncargo.sales.model.QuotePriceInfo;
import com.dragoncargo.sales.model.service.QuotePriceInfoService;
import com.polarj.common.web.controller.ModelController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quotepriceinfos")
public class QuotePriceInfoController
        extends ModelController<QuotePriceInfo, Integer, QuotePriceInfoService>
{
    public QuotePriceInfoController()
    {
        super(QuotePriceInfo.class);
    }
}
