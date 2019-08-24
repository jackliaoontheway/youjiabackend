package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.FieldSpecification;

public interface FieldSpecificationRepos extends JpaRepository<FieldSpecification, Integer>
{
    List<FieldSpecification> findByClassFullName(String classFullName);

    FieldSpecification findByFieldFullName(String fieldFullName);
}
