package com.polarj.model.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.polarj.TestBase;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountConfig;
import com.polarj.model.UserAccountRole;
import com.polarj.model.enumeration.UserAccountStatus;
import com.testing.model.SubSystemUser;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ModelServiceTest extends TestBase
{

    @SuppressWarnings("unchecked")
    private <M extends GenericDbInfo, S extends EntityService<M, Integer>> S getService(Class<S> serviceClass)
    {
        S service = null;

        try
        {
            service = (S) SpringContextUtils.getBean(serviceClass);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            service = null;
        }
        return service;
    }

    @Test
    void testUserAccountService()
    {
        UserAccountService service = getService(UserAccountService.class);
        Assertions.assertNotNull(service);
        saveSubSystenUserAccount(service);
    }

    void saveSubSystenUserAccount(UserAccountService service)
    {
        SubSystemUser userAcc = new SubSystemUser();
        userAcc.setLoginName("s02@test.com");
        userAcc.setPassword("asdasd123");
        userAcc.setStatus(UserAccountStatus.ACTIVE.name());
        userAcc.setSystemName("CRM");
        userAcc.setMemo("For Testing");
        UserAccountRoleService roleService = null;
        try
        {
            roleService = (UserAccountRoleService) SpringContextUtils.getBean(UserAccountRoleService.class);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            roleService = null;
        }
        Assertions.assertNotNull(roleService);
        UserAccountRole role = roleService.fetchByRoleCode("normal");
        if (role != null)
        {
            userAcc.addRole(role);
            UserAccountConfig defaultConf = role.getDefaultSetting();
            UserAccountConfig conf = new UserAccountConfig();
            conf.copy(defaultConf);
            userAcc.setUserSetting(conf);
        }
        service.create(userAcc, operId, languageId);
    }

    void removeSubSystemUserAccount(UserAccountService service)
    {
        UserAccount ua = service.fetchByName("s02@test.com");
        Assertions.assertNotNull(ua);
        service.delete(ua.getId(), operId);
    }

    void readSubDomainUserAccount(UserAccountService service)
    {
        UserAccount ua = service.getById(2, languageId);
        Assertions.assertNotNull(ua);
        if (StringUtils.isNotEmpty(ua.getSubClassName()))
        {
            try
            {
                @SuppressWarnings("unchecked")
                Class<? extends UserAccount> clazzSub = (Class<? extends UserAccount>) Class
                        .forName(ua.getSubClassName());
                UserAccount sUa = ua.castTo(clazzSub);
                logger.info(sUa.getEntityName());
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
