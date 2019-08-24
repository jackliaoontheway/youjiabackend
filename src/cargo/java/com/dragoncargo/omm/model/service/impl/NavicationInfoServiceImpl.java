package com.dragoncargo.omm.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.dragoncargo.omm.model.NavicationInfo;
import com.dragoncargo.omm.model.service.NavicationInfoService;

@Service
public class NavicationInfoServiceImpl extends EntityServiceImpl<NavicationInfo, Integer>
        implements NavicationInfoService
{
}
