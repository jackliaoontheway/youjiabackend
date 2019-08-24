package com.polarj.initial;

import org.apache.commons.lang3.StringUtils;

import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountConfig;
import com.polarj.model.UserAccountRole;
import com.polarj.model.service.UserAccountRoleService;
import com.polarj.model.service.UserAccountService;

public class InitUserAccount extends InitializeDataFromCSV<UserAccount, UserAccountService>
{

    private UserAccountRoleService roleService;

    public InitUserAccount()
    {
        super(UserAccount.class, UserAccountService.class);
        try
        {
            roleService = (UserAccountRoleService)SpringContextUtils.getBean(UserAccountRoleService.class);
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    @Override
    protected String initialData(boolean removeExistingData)
    {
        if (roleService == null)
        {
            return "Can not initialize the UserAccountRole Service. ";
        }
        return super.initialData(removeExistingData);
    }

    @Override
    protected String getCsvFileName()
    {
        return "useraccount.csv";
    }

    @Override
    protected int getCsvFileColumnAmount()
    {
        return 4;
    }

    @Override
    protected UserAccount convertFromCsvRow(String[] row)
    {
        if(roleService==null)
        {
            return null;
        }
        UserAccount userAcc = new UserAccount();
        userAcc.setLoginName(row[0]);
        userAcc.setPassword(row[1]);
        userAcc.setStatus(row[2]);
        UserAccountRole role = roleService.fetchByRoleCode(row[3]);
        if (role != null)
        {
            userAcc.addRole(role);
            UserAccountConfig defaultConf = role.getDefaultSetting();
            UserAccountConfig conf = new UserAccountConfig();
            conf.copy(defaultConf);
            userAcc.setUserSetting(conf);
        }
        return userAcc;
    }

    @Override
    protected String languageUsedInTheFile()
    {
        return "zh-cn";
    }

    @Override
    protected boolean isExisting(UserAccount entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getLoginName()))
        {
            return false;
        }
        UserAccount m = getService().fetchByName(entity.getLoginName());
        return m == null ? false : true;
    }

}
