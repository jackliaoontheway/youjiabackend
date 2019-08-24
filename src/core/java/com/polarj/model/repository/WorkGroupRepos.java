package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.UserAccount;
import com.polarj.model.WorkGroup;

public interface WorkGroupRepos extends JpaRepository<WorkGroup, Integer>
{
    List<WorkGroup> findByOwner(UserAccount owner);
}
