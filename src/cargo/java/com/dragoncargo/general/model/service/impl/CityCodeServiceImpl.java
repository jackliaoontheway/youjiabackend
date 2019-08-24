package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.CityCode;
import com.dragoncargo.general.model.service.CityCodeService;

@Service
public class CityCodeServiceImpl extends EntityServiceImpl<CityCode, Integer>
        implements CityCodeService
{
}
