package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.NavicationInfo;
import com.dragoncargo.omm.model.service.NavicationInfoService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/navicationinfos")
public class NavicationInfoController
        extends ModelController<NavicationInfo, Integer, NavicationInfoService>
{
    public NavicationInfoController()
    {
        super(NavicationInfo.class);
    }
}
