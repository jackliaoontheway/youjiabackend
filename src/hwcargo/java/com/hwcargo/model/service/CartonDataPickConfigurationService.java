package com.hwcargo.model.service;

import org.apache.poi.ss.usermodel.Sheet;

import com.hwcargo.model.CartonDataPickConfiguration;
import com.polarj.model.service.EntityService;

public interface CartonDataPickConfigurationService extends EntityService<CartonDataPickConfiguration, Integer>
{
    CartonDataPickConfiguration findConfigurationByExcel(Sheet sheet);
}
