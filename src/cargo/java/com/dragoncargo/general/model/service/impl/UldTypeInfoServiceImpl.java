package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.UldTypeInfo;
import com.dragoncargo.general.model.service.UldTypeInfoService;

@Service
public class UldTypeInfoServiceImpl extends EntityServiceImpl<UldTypeInfo, Integer>
        implements UldTypeInfoService
{
}
