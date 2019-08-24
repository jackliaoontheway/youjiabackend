package com.dragoncargo.customer.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.customer.model.CustomerCategory;
import com.dragoncargo.customer.model.service.CustomerCategoryService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/customercategorys")
public class CustomerCategoryController
        extends ModelController<CustomerCategory, Integer, CustomerCategoryService>
{
    public CustomerCategoryController()
    {
        super(CustomerCategory.class);
    }
}
