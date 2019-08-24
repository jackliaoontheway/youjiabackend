package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.AddressInfo;
import com.dragoncargo.general.model.service.AddressInfoService;

@Service
public class AddressInfoServiceImpl extends EntityServiceImpl<AddressInfo, Integer>
        implements AddressInfoService
{
}
