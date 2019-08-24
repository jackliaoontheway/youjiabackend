package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.BaseCode;
import com.dragoncargo.general.model.service.BaseCodeService;

@Service
public class BaseCodeServiceImpl extends EntityServiceImpl<BaseCode, Integer>
        implements BaseCodeService
{
}
