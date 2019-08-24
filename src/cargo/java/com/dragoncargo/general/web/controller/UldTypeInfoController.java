package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.UldTypeInfo;
import com.dragoncargo.general.model.service.UldTypeInfoService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/uldtypeinfos")
public class UldTypeInfoController
        extends ModelController<UldTypeInfo, Integer, UldTypeInfoService>
{
    public UldTypeInfoController()
    {
        super(UldTypeInfo.class);
    }
}
