package com.dragoncargo.customer.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.customer.model.CustomerType;
import com.dragoncargo.customer.model.service.CustomerTypeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/customertypes")
public class CustomerTypeController
        extends ModelController<CustomerType, Integer, CustomerTypeService>
{
    public CustomerTypeController()
    {
        super(CustomerType.class);
    }
}
