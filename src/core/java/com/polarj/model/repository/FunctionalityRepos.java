package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.Functionality;

public interface FunctionalityRepos extends JpaRepository<Functionality, Integer>
{
    Functionality findFirstByCode(String code);

    List<Functionality> findByCodeLike(String code);

    List<Functionality> findByTypeAndCodeLike(String type, String code);
}
