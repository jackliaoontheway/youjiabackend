package com.dragoncargo.omm.model.service;

import com.polarj.model.service.EntityService;
import com.dragoncargo.omm.model.PickupArea;

public interface PickupAreaService extends EntityService<PickupArea, Integer>
{

    PickupArea findByCode(String pickupAreaCode);
}
