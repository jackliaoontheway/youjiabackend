package com.dragoncargo.general.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.general.model.Airline;

import java.util.List;

public interface AirlineRepos extends JpaRepository<Airline, Integer>
{
    Airline findByCode(String code);
}
