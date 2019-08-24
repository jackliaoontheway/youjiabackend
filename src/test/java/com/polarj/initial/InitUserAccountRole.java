package com.polarj.initial;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.Functionality;
import com.polarj.model.UserAccountConfig;
import com.polarj.model.UserAccountRole;
import com.polarj.model.service.FunctionalityService;
import com.polarj.model.service.UserAccountRoleService;

public class InitUserAccountRole extends InitializeDataFromCSV<UserAccountRole, UserAccountRoleService>
{
    private FunctionalityService functionalityService;
    
    public InitUserAccountRole()
    {
        super(UserAccountRole.class, UserAccountRoleService.class);
        try
        {
            functionalityService = (FunctionalityService)SpringContextUtils.getBean(FunctionalityService.class);
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    @Override
    protected String initialData(boolean removeExistingData)
    {
        if (functionalityService == null)
        {
            return "Can not initialize the FunctionalityService.";
        }
        return super.initialData(removeExistingData);
    }

    @Override
    protected String getCsvFileName()
    {
        return "useraccountrole.csv";
    }

    @Override
    protected int getCsvFileColumnAmount()
    {
        return 7;
    }

    @Override
    protected UserAccountRole convertFromCsvRow(String[] row)
    {
        UserAccountRole uaRole = new UserAccountRole();
        uaRole.setCode(row[0]);
        uaRole.setLabel(row[1]);
        UserAccountConfig defaultCfg = new UserAccountConfig();
        defaultCfg.setViewLang(row[2]);
        defaultCfg.setWorkLang(row[3]);
        defaultCfg.setPageSize(Integer.parseInt(row[4]));
        defaultCfg.setFirstPageUrl(row[5]);
        uaRole.setDefaultSetting(defaultCfg);
        uaRole.setDefaultRole(Boolean.parseBoolean(row[6]));
        return uaRole;
    }

    @Override
    protected String languageUsedInTheFile()
    {
        return "zh-cn";
    }

    @Override
    protected String doSomethingAfterSavingEntities()
    {
        String roleFuncFileName = "role-func.csv";
        List<String[]> contents = readContentFromCsvFile(roleFuncFileName);
        if (contents == null || contents.size() == 0)
        {
            logger.info("No any functionality information for roles.");
            return null;
        }
        for (String[] row : contents)
        {
            if (row.length != 2)
            {
                logger.info("this record is not good enough for setting functionalities for role.");
                continue;
            }
            saveRoleFunctionalities(row[0], row[1]);
        }
        return null;
    }

    private void saveRoleFunctionalities(String roleCode, String funcs)
    {
        UserAccountRole uaRole = getService().fetchByRoleCode(roleCode);
        if (uaRole == null)
        {
            logger.info("There is no user account role with code: {}.", roleCode);
            return;
        }
        String[] codePatterns = funcs.split(";");
        if (codePatterns == null || codePatterns.length == 0)
        {
            logger.info("There is no functionality information in: {}", funcs);
            return;
        }
        List<Functionality> roleFuncs = new ArrayList<Functionality>();
        for (String codePattern : codePatterns)
        {
            List<Functionality> fs = functionalityService.fetchFunctionalityByCodePattren(codePattern, languageId);
            if (fs == null || fs.size() == 0)
            {
                logger.info("there is no functionality with code pattern: {}.", codePattern);
                continue;
            }
            for (Functionality f : fs)
            {
                boolean hasFunc = false;
                for (Functionality rf : roleFuncs)
                {
                    if (rf.getCode().equals(f.getCode()))
                    {
                        hasFunc = true;
                    }
                }
                if (!hasFunc)
                {
                    roleFuncs.add(f);
                }
            }
        }
        if (roleFuncs.size() > 0)
        {
            uaRole.setFunctions(roleFuncs);
            getService().update(uaRole.getId(), uaRole, operId, languageUsedInTheFile());
        }
    }

    // FIXME: 角色模型已经修改为带owner的了，这里判断存在性就需要跟owner打交到吧？
    @Override
    protected boolean isExisting(UserAccountRole entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getCode()) || entity.getOwner()==null)
        {
            return false;
        }
        UserAccountRole m = getService().fetchByRoleCode(entity.getCode());
        return m == null ? false : true;
    }
}
