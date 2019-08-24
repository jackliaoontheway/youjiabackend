package com.polarj.initial;

import org.apache.commons.lang3.StringUtils;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.service.EntityService;

public abstract class InitializeDataFromFile<M extends GenericDbInfo, S extends EntityService<M, Integer>>
        extends AbstractInitializeData<M, S>
{

    InitializeDataFromFile(Class<M> modelClass, Class<S> serviceClass)
    {
        super(modelClass, serviceClass);
    }

    @Override
    protected String initialData(boolean removeExistingData)
    {
        if (StringUtils.isEmpty(languageUsedInTheFile()))
        {
            return "No language was set for " + getModelClass().getSimpleName();
        }

        if (StringUtils.isEmpty(getDataSourceFile()))
        {
            return "No data source file name setted.";
        }

        return super.initialData(removeExistingData);
    }

    protected String languageUsedInDataSource()
    {
        return languageUsedInTheFile();
    }

    abstract protected String getDataSourceFile();

    abstract protected String languageUsedInTheFile();

}
