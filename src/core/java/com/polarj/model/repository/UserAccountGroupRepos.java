package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountGroup;

public interface UserAccountGroupRepos extends JpaRepository<UserAccountGroup, Integer>
{
    List<UserAccountGroup> findByOwner(UserAccount owner);
}
