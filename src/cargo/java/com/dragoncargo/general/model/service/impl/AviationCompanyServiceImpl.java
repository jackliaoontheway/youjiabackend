package com.dragoncargo.general.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.general.model.AviationCompany;
import com.dragoncargo.general.model.service.AviationCompanyService;

@Service
public class AviationCompanyServiceImpl extends EntityServiceImpl<AviationCompany, Integer>
        implements AviationCompanyService
{
}
