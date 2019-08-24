package com.dragoncargo.omm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.omm.model.PickupArea;

public interface PickupAreaRepos extends JpaRepository<PickupArea, Integer>
{

    PickupArea findFirstByCode(String pickupAreaCode);
}
