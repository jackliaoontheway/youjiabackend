package com.polarj.model.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountRole;

public interface UserAccountService extends EntityService<UserAccount, Integer>
{
    @Transactional
    UserAccount fetchByName(String loginName);

    @Transactional
    UserAccount fetchByHashedName(String hashedLoginName);

    @Transactional
    Boolean modifyPassword(String loginName, String originalPassword, String newPassword);
    
    void generateUserAccFunctionalities(UserAccount userAcc, List<UserAccountRole> roles);

    void addSuperAdminFlag(UserAccount userAcc);
}
