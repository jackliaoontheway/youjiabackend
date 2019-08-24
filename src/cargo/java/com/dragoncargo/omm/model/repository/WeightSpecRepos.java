package com.dragoncargo.omm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dragoncargo.omm.model.WeightSpec;

public interface WeightSpecRepos extends JpaRepository<WeightSpec, Integer>
{
    WeightSpec findByCode(String code);
}
