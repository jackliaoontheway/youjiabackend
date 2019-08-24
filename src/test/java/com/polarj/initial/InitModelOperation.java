package com.polarj.initial;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.polarj.PolarjHibernateConfig;
import com.polarj.common.utility.ReflectionUtil;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.Functionality;
import com.polarj.model.UserAccountRole;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FunctionType;
import com.polarj.model.enumeration.ModelOperation;
import com.polarj.model.service.FunctionalityService;
import com.polarj.model.service.UserAccountRoleService;

public class InitModelOperation extends AbstractInitializeData<Functionality, FunctionalityService>
{
    private UserAccountRoleService roleService;
    
    private PolarjHibernateConfig config;

    InitModelOperation()
    {
        super(Functionality.class, FunctionalityService.class);
        try
        {
            roleService = (UserAccountRoleService) SpringContextUtils.getBean(UserAccountRoleService.class);
            config = (PolarjHibernateConfig)SpringContextUtils.getBean(PolarjHibernateConfig.class);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    protected String initialData(boolean removeExistingData)
    {
        if(roleService == null)
        {
            return "No service injected for: UserAccountRoleService.";
        }
        if(config == null)
        {
            return "No config injected for: PolarjHibernateConfig.";
        }
        return super.initialData(removeExistingData);
    }

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
            saveRoleModelOperation(row[0]);
        }
        return null;
    }

    private void saveRoleModelOperation(String roleCode)
    {
        UserAccountRole uaRole = roleService.fetchByRoleCode(roleCode);
        if (uaRole == null)
        {
            logger.info("There is no user account role with code: {}.", roleCode);
            return;
        }
        if (uaRole.getCode().equals("admin"))
        {
            return;
        }
        List<Functionality> fs = getService().fetchFunctionalityByCodePattren(".READ", languageId);
        if (fs.size() > 0)
        {
            List<Functionality> roleFuncs = uaRole.getFunctions();
            if (roleFuncs != null)
            {
                roleFuncs.addAll(fs);
            }
            else
            {
                roleFuncs = fs;
                uaRole.setFunctions(roleFuncs);
            }
            roleService.update(uaRole.getId(), uaRole, operId, languageId);
        }
    }

    @Override
    protected List<Functionality> fetchDataFromDataSource()
    {
        List<Functionality> functionalities = new ArrayList<>();
        for (String modelPackage : config.getPackagesToScan())
        {
            List<Class<?>> classes = ReflectionUtil.listClassWithAnnotationUnderPackage(modelPackage,
                    ModelMetaData.class);
            if (classes == null || classes.size() == 0)
            {
                logger.info("No model found under: " + modelPackage);
                continue;
            }
            for (Class<?> clazz : classes)
            {
                for (ModelOperation o : ModelOperation.values())
                {
                    Functionality func = new Functionality();
                    func.setCode(clazz.getName() + "." + o.name());
                    func.setType(FunctionType.MODEL_OPERATION.name());
                    func.setPositionSn(0);
                    func.setLabel(o.name().substring(0, 1) + o.name().substring(1).toLowerCase() + " "
                            + clazz.getSimpleName());
                    functionalities.add(func);
                }
            }
        }
        return functionalities;
    }

    @Override
    protected String languageUsedInDataSource()
    {
        return languageId;
    }

    @Override
    protected boolean isExisting(Functionality entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getCode()))
        {
            return false;
        }
        Functionality m = getService().fetchFunctionalityByCode(entity.getCode(), languageUsedInDataSource());
        return m == null ? false : true;
    }

}
