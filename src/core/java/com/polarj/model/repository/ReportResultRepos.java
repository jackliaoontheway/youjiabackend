package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polarj.model.ReportResult;

public interface ReportResultRepos extends JpaRepository<ReportResult, Integer>
{
}
