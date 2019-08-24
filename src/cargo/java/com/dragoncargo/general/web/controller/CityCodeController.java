package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.CityCode;
import com.dragoncargo.general.model.service.CityCodeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/citycodes")
public class CityCodeController
        extends ModelController<CityCode, Integer, CityCodeService>
{
    public CityCodeController()
    {
        super(CityCode.class);
    }
}
