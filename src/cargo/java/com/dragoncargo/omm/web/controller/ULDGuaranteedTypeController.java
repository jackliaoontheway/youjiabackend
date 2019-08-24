package com.dragoncargo.omm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.omm.model.ULDGuaranteedType;
import com.dragoncargo.omm.model.service.ULDGuaranteedTypeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/uldguaranteedtypes")
public class ULDGuaranteedTypeController
        extends ModelController<ULDGuaranteedType, Integer, ULDGuaranteedTypeService>
{
    public ULDGuaranteedTypeController()
    {
        super(ULDGuaranteedType.class);
    }
}
