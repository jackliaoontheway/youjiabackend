package com.dragoncargo.omm.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragoncargo.omm.model.PickupArea;
import com.dragoncargo.omm.model.PickupLocation;

public interface PickupLocationRepos extends JpaRepository<PickupLocation, Integer>
{

    List<PickupLocation> findByPickupArea(PickupArea pickupArea);
}
