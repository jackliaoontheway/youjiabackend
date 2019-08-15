package com.youjia.model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youjia.model.WaterElectricityRecord;
import com.youjia.model.service.WaterElectricityRecordService;
import com.polarj.common.web.controller.ModelController;

@RestController
@RequestMapping("/waterelectricityrecords")
public class WaterElectricityRecordController
        extends ModelController<WaterElectricityRecord, Integer, WaterElectricityRecordService>
{
    public WaterElectricityRecordController()
    {
        super(WaterElectricityRecord.class);
    }
}
