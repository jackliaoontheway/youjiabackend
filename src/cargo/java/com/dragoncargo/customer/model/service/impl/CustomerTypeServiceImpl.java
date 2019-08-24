package com.dragoncargo.customer.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.customer.model.CustomerType;
import com.dragoncargo.customer.model.service.CustomerTypeService;

@Service
public class CustomerTypeServiceImpl extends EntityServiceImpl<CustomerType, Integer>
        implements CustomerTypeService
{
}
