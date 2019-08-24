package com.dragoncargo.omm.model.service.impl;

import org.springframework.stereotype.Service;

import com.dragoncargo.omm.model.PickupArea;
import com.dragoncargo.omm.model.repository.PickupAreaRepos;
import com.dragoncargo.omm.model.service.PickupAreaService;
import com.polarj.model.service.impl.EntityServiceImpl;

@Service
public class PickupAreaServiceImpl extends EntityServiceImpl<PickupArea, Integer> implements PickupAreaService
{

    @Override
    public PickupArea findByCode(String pickupAreaCode)
    {
        PickupAreaRepos repos = (PickupAreaRepos) getRepos();

        return repos.findFirstByCode(pickupAreaCode);
    }
}
