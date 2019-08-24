package com.polarj.initial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.service.EntityService;

abstract class InitializeDataFromJSON<M extends GenericDbInfo, S extends EntityService<M, Integer>>
        extends InitializeDataFromFile<M, S>
{
    private Class<M[]> modelArrayClass;

    InitializeDataFromJSON(Class<M> modelClass, Class<M[]> modelArrayClass, Class<S> serviceClass)
    {
        super(modelClass, serviceClass);
        this.modelArrayClass = modelArrayClass;
    }

    @Override
    protected String initialData(boolean removeExistingData)
    {
        if (StringUtils.isEmpty(getJSONFileName()))
        {
            return "No json file name was set for " + getModelClass().getSimpleName();
        }
        return super.initialData(removeExistingData);
    }

    @Override
    protected List<M> fetchDataFromDataSource()
    {
        List<M> entities = null;
        try
        {
            entities = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            File src = new File(getInitDataPath() + getDataSourceFile());
            M[] datas = objectMapper.readValue(src, modelArrayClass);
            if (datas == null || datas.length == 0)
            {
                return entities;
            }
            CollectionUtils.addAll(entities, datas);
        }
        catch (Exception e)
        {
            entities = null;
            logger.error(e.getMessage(), e);
        }
        return entities;
    }

    @Override
    protected String getDataSourceFile()
    {
        return getJSONFileName();
    }

    abstract protected String getJSONFileName();
}
