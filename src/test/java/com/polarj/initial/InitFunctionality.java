package com.polarj.initial;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.polarj.common.CommonConstant;
import com.polarj.model.Functionality;
import com.polarj.model.service.FunctionalityService;

class InitFunctionality extends InitializeDataFromCSV<Functionality, FunctionalityService>
{
    public InitFunctionality()
    {
        super(Functionality.class, FunctionalityService.class);
    }

    @Override
    protected String getCsvFileName()
    {
        return "functionality.csv";
    }

    @Override
    protected int getCsvFileColumnAmount()
    {
        return 6;
    }

    @Override
    protected Functionality convertFromCsvRow(String[] row)
    {
        Functionality f = new Functionality();
        f.setCode(row[0]);
        f.setPositionSn(Integer.parseInt(row[1]));
        f.setLabel(row[2]);
        f.setPathUrl(row[3]);
        f.setType(row[4]);
        f.setIconName(row[5]);
        return f;
    }

    @Override
    protected String languageUsedInTheFile()
    {
        return "zh-cn";
    }

    protected String doSomethingAfterSavingEntities()
    {
        List<Functionality> entities = getService().list(languageUsedInTheFile());
        List<Functionality> parentMenus = new ArrayList<Functionality>();
        for (Functionality entity : entities)
        {
            if (entity.getCode().split("\\.").length == 2)
            {
                parentMenus.add(entity);
            }
        }

        for (Functionality entity : entities)
        {
            for (Functionality parentMenu : parentMenus)
            {
                if (entity.getCode().startsWith(parentMenu.getCode() + "."))
                {
                    entity.setParentMenu(parentMenu);
                    getService().update(entity.getId(), entity, CommonConstant.systemUserAccountId,
                            languageUsedInTheFile());
                }
            }
        }
        return null;
    }

    @Override
    protected boolean isExisting(Functionality entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getCode()))
        {
            return false;
        }
        Functionality m = getService().fetchFunctionalityByCode(entity.getCode(), languageUsedInTheFile());
        return m == null ? false : true;
    }
}
