package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.ReportDesc;

public interface ReportDescRepos extends JpaRepository<ReportDesc, Integer>
{
    ReportDesc findFirstByCode(String code);
}
