package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.AddressInfo;
import com.dragoncargo.general.model.service.AddressInfoService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/addressinfos")
public class AddressInfoController
        extends ModelController<AddressInfo, Integer, AddressInfoService>
{
    public AddressInfoController()
    {
        super(AddressInfo.class);
    }
}
