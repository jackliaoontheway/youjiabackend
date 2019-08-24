package com.hwcargo.service.impl;

import org.apache.poi.ss.usermodel.Sheet;

import com.hwcargo.model.CartonDataPickConfiguration;

public interface CartonDataPickStrategy
{

    CartonDataPickResponse generateCartonData(Sheet sheet, CartonDataPickConfiguration configuration);

}
