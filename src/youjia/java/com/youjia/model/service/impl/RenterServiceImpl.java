package com.youjia.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.Renter;
import com.youjia.model.service.RenterService;

@Service
public class RenterServiceImpl extends EntityServiceImpl<Renter, Integer>
        implements RenterService
{
}
