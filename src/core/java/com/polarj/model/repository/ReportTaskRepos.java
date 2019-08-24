package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polarj.model.ReportTask;

public interface ReportTaskRepos extends JpaRepository<ReportTask, Integer>
{
}
