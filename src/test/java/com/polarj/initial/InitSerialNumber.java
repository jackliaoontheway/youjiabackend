package com.polarj.initial;

import org.apache.commons.lang3.StringUtils;

import com.polarj.model.SerialNumber;
import com.polarj.model.service.SerialNumberService;

public class InitSerialNumber extends InitializeDataFromCSV<SerialNumber, SerialNumberService>
{

    public InitSerialNumber()
    {
        super(SerialNumber.class, SerialNumberService.class);
    }

    @Override
    protected String getCsvFileName()
    {
        return "serialnumber.csv";
    }

    @Override
    protected int getCsvFileColumnAmount()
    {
        return 5;
    }

    @Override
    protected SerialNumber convertFromCsvRow(String[] row)
    {
        SerialNumber entity = new SerialNumber();
        entity.setModuleCode(row[0]);
        entity.setDescription(row[1]);
        entity.setSnPattern(row[2]);
        entity.setCurrentSn(Long.valueOf(row[3]));
        entity.setInCacheSnAmount(Integer.valueOf(row[4]));
        return entity;
    }

    @Override
    protected String languageUsedInTheFile()
    {
        return "zh-cn";
    }

    @Override
    protected boolean isExisting(SerialNumber entity)
    {
        if (entity == null || StringUtils.isEmpty(entity.getModuleCode()))
        {
            return false;
        }
        SerialNumber m = getService().fetchSNForModule(entity.getModuleCode());
        return m == null ? false : true;
    }

}
