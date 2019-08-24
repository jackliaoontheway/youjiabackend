package com.dragoncargo.customer.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.customer.model.CustomerLevel;
import com.dragoncargo.customer.model.service.CustomerLevelService;

@Service
public class CustomerLevelServiceImpl extends EntityServiceImpl<CustomerLevel, Integer>
        implements CustomerLevelService
{
}
