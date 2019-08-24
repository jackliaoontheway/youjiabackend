package com.polarj.initial;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.service.EntityService;

abstract class InitializeDataFromCSV<M extends GenericDbInfo, S extends EntityService<M, Integer>>
        extends InitializeDataFromFile<M, S>
{
    public InitializeDataFromCSV(Class<M> modelClass, Class<S> serviceClass)
    {
        super(modelClass, serviceClass);
    }

    @Override
    protected String initialData(boolean removeExistingData)
    {
        if (StringUtils.isEmpty(getCsvFileName()))
        {
            return "No csv file name was set for " + getModelClass().getSimpleName();
        }
        if (getCsvFileColumnAmount() == 0)
        {
            return "No record column amount was set for " + getModelClass().getSimpleName();
        }
        return super.initialData(removeExistingData);
    }

    @Override
    protected String getDataSourceFile()
    {
        return getCsvFileName();
    }

    @Override
    protected List<M> fetchDataFromDataSource()
    {
        List<M> entities = new ArrayList<>();
        List<String[]> contents = readContentFromCsvFile(getCsvFileName());
        for (String[] row : contents)
        {
            if (row.length != getCsvFileColumnAmount())
            {
                logger.error("this row is not a illegal data: " + row.length + ". but it should be "
                        + getCsvFileColumnAmount());
                continue;
            }
            M record = convertFromCsvRow(row);
            if (record != null)
            {
                entities.add(record);
            }
        }
        return entities;
    }

    abstract protected String getCsvFileName();

    abstract protected int getCsvFileColumnAmount();

    abstract protected M convertFromCsvRow(String[] row);
}
