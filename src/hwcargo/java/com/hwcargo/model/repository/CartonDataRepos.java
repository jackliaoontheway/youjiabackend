package com.hwcargo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hwcargo.model.CartonData;

public interface CartonDataRepos extends JpaRepository<CartonData, Integer>
{

    List<CartonData> findByCartonName(String cartonName);
}
