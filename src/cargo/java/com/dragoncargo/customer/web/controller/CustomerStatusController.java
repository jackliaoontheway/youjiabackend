package com.dragoncargo.customer.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.customer.model.CustomerStatus;
import com.dragoncargo.customer.model.service.CustomerStatusService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/customerstatuss")
public class CustomerStatusController
        extends ModelController<CustomerStatus, Integer, CustomerStatusService>
{
    public CustomerStatusController()
    {
        super(CustomerStatus.class);
    }
}
