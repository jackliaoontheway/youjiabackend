package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.AviationAreaCode;
import com.dragoncargo.general.model.service.AviationAreaCodeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/aviationareacodes")
public class AviationAreaCodeController
        extends ModelController<AviationAreaCode, Integer, AviationAreaCodeService>
{
    public AviationAreaCodeController()
    {
        super(AviationAreaCode.class);
    }
}
