package com.youjia.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.youjia.model.WaterElectricityRecord;
import com.youjia.model.service.WaterElectricityRecordService;

@Service
public class WaterElectricityRecordServiceImpl extends EntityServiceImpl<WaterElectricityRecord, Integer>
        implements WaterElectricityRecordService
{
}
