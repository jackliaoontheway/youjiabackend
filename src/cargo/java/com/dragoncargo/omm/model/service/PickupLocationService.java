package com.dragoncargo.omm.model.service;

import java.util.List;

import com.dragoncargo.omm.model.PickupLocation;
import com.polarj.model.service.EntityService;

public interface PickupLocationService extends EntityService<PickupLocation, Integer>
{
    
    List<PickupLocation> listByPickupAreaCode(String pickupAreaCode);
    
}
