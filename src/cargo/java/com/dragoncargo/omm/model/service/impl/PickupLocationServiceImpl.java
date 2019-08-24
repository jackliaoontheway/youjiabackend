package com.dragoncargo.omm.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.PickupArea;
import com.dragoncargo.omm.model.PickupLocation;
import com.dragoncargo.omm.model.repository.PickupLocationRepos;
import com.dragoncargo.omm.model.service.PickupAreaService;
import com.dragoncargo.omm.model.service.PickupLocationService;
import com.polarj.model.service.impl.EntityServiceImpl;

@Service
public class PickupLocationServiceImpl extends EntityServiceImpl<PickupLocation, Integer>
        implements PickupLocationService
{

    @Autowired
    private PickupAreaService pickupAreaService;
    
    @Override
    public List<PickupLocation> listByPickupAreaCode(String pickupAreaCode)
    {
        PickupArea pickupArea =  pickupAreaService.findByCode(pickupAreaCode);
        
        PickupLocationRepos repose = (PickupLocationRepos)getRepos();
        
        return repose.findByPickupArea(pickupArea);
    }
}
