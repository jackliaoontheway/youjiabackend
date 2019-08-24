package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.AviationAreaCode;
import com.dragoncargo.general.model.service.AviationAreaCodeService;

@Service
public class AviationAreaCodeServiceImpl extends EntityServiceImpl<AviationAreaCode, Integer>
        implements AviationAreaCodeService
{
}
