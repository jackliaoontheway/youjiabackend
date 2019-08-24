package com.polarj.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polarj.model.OperationInfo;

public interface OperationInfoRepos extends JpaRepository<OperationInfo, Integer>
{
}
