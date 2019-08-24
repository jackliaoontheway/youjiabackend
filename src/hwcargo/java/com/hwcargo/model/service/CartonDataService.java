package com.hwcargo.model.service;

import java.util.List;

import com.hwcargo.model.CartonData;
import com.polarj.model.service.EntityService;

public interface CartonDataService extends EntityService<CartonData, Integer>
{

    List<CartonData> findByCartonName(String cartonName);
}
