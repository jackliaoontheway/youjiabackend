package com.dragoncargo.customer.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.customer.model.CustomerStatus;
import com.dragoncargo.customer.model.service.CustomerStatusService;

@Service
public class CustomerStatusServiceImpl extends EntityServiceImpl<CustomerStatus, Integer>
        implements CustomerStatusService
{
}
