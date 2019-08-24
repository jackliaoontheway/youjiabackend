package com.polarj.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarj.model.OperationInfo;
import com.polarj.model.service.OperationInfoService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/operationinfos")
public class OperationInfoController
        extends ModelController<OperationInfo, Integer, OperationInfoService>
{
    public OperationInfoController()
    {
        super(OperationInfo.class);
    }
}
