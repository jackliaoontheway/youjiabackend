package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.RentBill;
import com.youjia.model.service.RentBillService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/rentbills")
public class RentBillController
        extends ModelController<RentBill, Integer, RentBillService>
{
    public RentBillController()
    {
        super(RentBill.class);
    }
}
