package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.ModelSpecification;

public interface ModelSpecificationRepos extends JpaRepository<ModelSpecification, Integer>
{
    ModelSpecification findOneByClassName(String className);
}
