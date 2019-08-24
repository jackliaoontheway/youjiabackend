package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;

public interface UserAccountRoleRepos extends JpaRepository<UserAccountRole, Integer>
{

    UserAccountRole findByCode(String roleCode);

    UserAccountRole findByDefaultRole(boolean isDefaultRole);

    List<UserAccountRole> findByOwner(UserAccount owner);
}
