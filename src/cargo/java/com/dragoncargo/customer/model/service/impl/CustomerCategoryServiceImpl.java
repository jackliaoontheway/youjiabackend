package com.dragoncargo.customer.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.customer.model.CustomerCategory;
import com.dragoncargo.customer.model.service.CustomerCategoryService;

@Service
public class CustomerCategoryServiceImpl extends EntityServiceImpl<CustomerCategory, Integer>
        implements CustomerCategoryService
{
}
