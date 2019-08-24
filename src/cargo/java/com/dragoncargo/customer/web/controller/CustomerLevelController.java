package com.dragoncargo.customer.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.customer.model.CustomerLevel;
import com.dragoncargo.customer.model.service.CustomerLevelService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/customerlevels")
public class CustomerLevelController
        extends ModelController<CustomerLevel, Integer, CustomerLevelService>
{
    public CustomerLevelController()
    {
        super(CustomerLevel.class);
    }
}
