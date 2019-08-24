package com.polarj.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polarj.model.UserAccount;

public interface UserAccountRepos extends JpaRepository<UserAccount, Integer>
{
    UserAccount findByLoginName(String loginName);

    UserAccount findByHashedLoginName(String hashedLoginName);

    List<UserAccount> findByOwner(UserAccount owner);
}
