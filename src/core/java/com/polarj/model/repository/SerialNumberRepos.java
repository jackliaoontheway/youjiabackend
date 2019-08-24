package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polarj.model.SerialNumber;

public interface SerialNumberRepos extends JpaRepository<SerialNumber, Integer>
{
    SerialNumber findByModuleCode(String moduleCode);
}
