package com.youjia.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Lease;
import com.youjia.model.service.LeaseService;

@Service
public class LeaseServiceImpl extends EntityServiceImpl<Lease, Integer>
        implements LeaseService
{
}
