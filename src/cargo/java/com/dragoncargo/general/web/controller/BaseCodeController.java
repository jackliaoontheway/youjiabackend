package com.dragoncargo.general.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragoncargo.general.model.BaseCode;
import com.dragoncargo.general.model.service.BaseCodeService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/basecodes")
public class BaseCodeController
        extends ModelController<BaseCode, Integer, BaseCodeService>
{
    public BaseCodeController()
    {
        super(BaseCode.class);
    }
}
