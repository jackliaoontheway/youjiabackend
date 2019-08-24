package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.DivisionGroup;
import com.polarj.model.UserAccount;

public interface DivisionGroupRepos extends JpaRepository<DivisionGroup, Integer>
{
    List<DivisionGroup> findByOwner(UserAccount owner);
}
