package com.polarj.initial;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.polarj.TestBase;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.service.EntityService;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractInitializeData<M extends GenericDbInfo, S extends EntityService<M, Integer>>
        extends TestBase
{
    private @Setter(value = lombok.AccessLevel.PROTECTED) @Getter(value = lombok.AccessLevel.PROTECTED) S service;

    private @Getter(value = lombok.AccessLevel.PROTECTED) Class<M> modelClass;

    @SuppressWarnings("unchecked")
    AbstractInitializeData(Class<M> modelClass, Class<S> serviceClass)
    {
        this.modelClass = modelClass;
        S service = null;
        try
        {
            service = (S) SpringContextUtils.getBean(serviceClass);
            setService(service);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    final public String removeExistingDataThenInitialization()
    {
        return initialData(true);
    }

    final public String addNewDataOnly()
    {
        return initialData(false);
    }

    protected String initialData(boolean removeExistingData)
    {
        if (getService() == null)
        {
            return "Can not initialize the Service for " + getModelClass().getSimpleName();
        }
        if (removeExistingData && hasData())
        { // 如果是需要删除现有数据，同时系统中又有数据，那么删除数据库中的所有数据
            removeAllExistingData();
        }
        List<M> entities = fetchDataFromDataSource();
        if (CollectionUtils.isEmpty(entities))
        {
            return "Do not fetch any data from data source.";
        }
        for (M entityFromDS : entities)
        {
            // 如果不是在删除之后再初始化数据，那么系统中有可能存在读取回来的数据
            if (removeExistingData || !isExisting(entityFromDS))
            {
                M savedEntity = service.create(entityFromDS, operId, languageUsedInDataSource());
                if (savedEntity == null)
                {
                    return "Create new data error.";
                }
            }
        }
        String msg = doSomethingAfterSavingEntities();
        return msg;
    }

    private boolean removeAllExistingData()
    {
        long count = getService().count();
        Pageable p = PageRequest.of(0, 100);
        while (count > 0)
        {
            Page<M> modelPages = getService().list(p, languageId);
            getService().delete(modelPages.getContent(), operId);
            count = modelPages.getTotalElements() - 100;
        }
        return true;
    }

    private boolean hasData()
    {
        long amt = getService().count();
        return amt > 0;
    }

    protected String doSomethingAfterSavingEntities()
    {
        // 在保存完当前业务模型之后还要做一些相关处理的时候，可以在子类重新实现这个方法。
        return null;
    }

    abstract protected List<M> fetchDataFromDataSource();

    abstract protected String languageUsedInDataSource();

    // 判断该数据是否在数据库中存在
    // 在基类中判断，如果是返回的null，那么说明该类还没有被子类重写
    abstract protected boolean isExisting(M entity);
}
