package com.hwcargo.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hwcargo.model.CartonData;
import com.hwcargo.model.repository.CartonDataRepos;
import com.hwcargo.model.service.CartonDataService;
import com.polarj.model.service.impl.EntityServiceImpl;

@Service
public class CartonDataServiceImpl extends EntityServiceImpl<CartonData, Integer> implements CartonDataService
{

    @Override
    public List<CartonData> findByCartonName(String cartonName)
    {
        CartonDataRepos repos = (CartonDataRepos) this.getRepos();
        return repos.findByCartonName(cartonName);
    }
}
