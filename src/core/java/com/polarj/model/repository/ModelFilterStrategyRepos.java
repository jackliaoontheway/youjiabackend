package com.polarj.model.repository;

import com.polarj.model.ModelFilterStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelFilterStrategyRepos extends JpaRepository<ModelFilterStrategy, Integer>
{
    List<ModelFilterStrategy> findByClassFullName(String classFullName);
}
