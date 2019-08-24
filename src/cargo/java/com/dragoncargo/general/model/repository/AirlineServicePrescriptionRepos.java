package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.AirlineServicePrescription;

public interface AirlineServicePrescriptionRepos extends JpaRepository<AirlineServicePrescription, Integer>
{
}
