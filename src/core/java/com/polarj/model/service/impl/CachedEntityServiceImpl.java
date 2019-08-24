package com.polarj.model.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;

import com.polarj.common.Cacheable;
import com.polarj.common.CommonConstant;
import com.polarj.common.utility.FieldValueUtil;
import com.polarj.model.GenericDbInfo;

import lombok.Setter;

public abstract class CachedEntityServiceImpl<T extends GenericDbInfo> extends EntityServiceImpl<T, Integer>
        implements Cacheable
{
    private TreeMap<Integer, T> cachedEntitiesById = new TreeMap<>();

    private TreeMap<String, T> cachedEntitiesByUniqueField = new TreeMap<>();

    private @Setter Field[] uniqueFields;

    @Override
    public void delete(Integer id, Integer operId)
    {
        super.delete(id, operId);
        if (id != null)
        {
            T t = cachedEntitiesById.get(id);
            if (t != null)
            {
                removeFromCache(t);
            }
        }
    }

    @Override
    public void delete(List<T> records, Integer operId)
    {
        super.delete(records, operId);
        if (records == null || records.size() == 0)
        {
            return;
        }
        for (T record : records)
        {
            removeFromCache(record);
        }
    }

    @Override
    public T update(Integer entityId, T entityWithUpdatedInfo, Integer operId, String languageId)
    {
        T res = super.update(entityId, entityWithUpdatedInfo, operId, languageId);
        updateCache(res);
        return res;
    }

    @Override
    public List<T> list(int limit, String languageId)
    {
        loadCache();
        List<T> res = new ArrayList<T>();
        int i = 0;
        SortedSet<Integer> ss = cachedEntitiesById.navigableKeySet();
        for (Integer key : ss)
        {
            res.add(cachedEntitiesById.get(key));
            i++;
            if (i >= limit)
            {
                break;
            }
        }
        return replaceI18nFieldValueWithResource(res, languageId);
    }

    @Override
    public List<T> list(String languageId)
    {
        loadCache();
        List<T> res = new ArrayList<T>(cachedEntitiesById.size());
        for(T entity: cachedEntitiesById.values())
        {
            res.add(entity);
        }
        return replaceI18nFieldValueWithResource(res, languageId);
    }

    @Override
    public List<T> list(List<Integer> ids, String languageId)
    {
        loadCache();
        if (ids == null || ids.size() == 0)
        {
            return null;
        }
        List<T> res = new ArrayList<T>();
        for (Integer key : ids)
        {
            res.add(cachedEntitiesById.get(key));
        }
        return replaceI18nFieldValueWithResource(res, languageId);
    }

    @Override
    public T getById(Integer id, String languageId)
    {
        return getById(id, 2, languageId);
    }

    @Override
    public T getById(Integer id, int deepth, String languageId)
    {
        return fetchFromCacheById(id, languageId);
    }

    @Override
    public T create(T entity, Integer operId, String languageId)
    {
        if (entity != null && entity.getId() == null)
        {
            T res = super.create(entity, operId, languageId);
            addIntoCache(res);
            return res;
        }
        return null;
    }

    public void clearCache()
    {
        cachedEntitiesById.clear();
        cachedEntitiesByUniqueField.clear();
    }

    public void reloadCache()
    {
        clearCache();
        loadCache();
    }

    //FIXME: 这个方法可以去掉，直接使用unique的索引列作为唯一值检索键值即可
    abstract protected void setUniqueField();

    private void loadCache()
    {
        if(uniqueFields==null)
        {
            setUniqueField();
        }
        List<T> entities = null;
        if (cachedEntitiesById.size() == 0)
        {
            entities = super.list(CommonConstant.defaultSystemLanguage);
            for (T entity : entities)
            {
                cachedEntitiesById.put(entity.getId(), entity);
            }
        }
        if (cachedEntitiesById.size() == 0)
        {
            return;
        }
        if (uniqueFields == null || uniqueFields.length == 0)
        {
            return;
        }
        if (cachedEntitiesByUniqueField.size() == 0)
        {
            for (T entity : cachedEntitiesById.values())
            {
                addUniqueFieldCache(entity);
            }
        }
    }

    private void addUniqueFieldCache(T entity)
    {
        if (uniqueFields == null || uniqueFields.length == 0)
        {
            return;
        }
        String key = "";
        for (Field uniqueField : uniqueFields)
        {
            key = key + FieldValueUtil.fetchFieldValue(entity, uniqueField, logger) + "-";
        }
        if (key.length() > 0)
        {
            cachedEntitiesByUniqueField.put(key, entity);
        }
    }

    private void removeUniqueFieldCache(T entity)
    {
        if (uniqueFields == null || uniqueFields.length == 0)
        {
            return;
        }
        String key = "";
        for (Field uniqueField : uniqueFields)
        {
            key = key + FieldValueUtil.fetchFieldValue(entity, uniqueField, logger) + "-";
        }
        if (key.length() > 0)
        {
            cachedEntitiesByUniqueField.remove(key);
        }
    }

    private void removeFromCache(T record)
    {
        loadCache();
        if (record.getId() == null)
        {
            return;
        }
        if (cachedEntitiesById.containsKey(record.getId()))
        {
            cachedEntitiesById.remove(record.getId());
            removeUniqueFieldCache(record);
        }
    }

    private void addIntoCache(T record)
    {
        loadCache();
        saveCache(record);
    }

    private void updateCache(T record)
    {
        loadCache();
        saveCache(record);
    }

    protected T fetchFromCacheById(Integer id, String languageId)
    {
        loadCache();
        if (id == null)
        {

            return null;
        }
        return replaceI18nFieldValueWithResource(cachedEntitiesById.get(id), languageId);
    }

    protected T fetchFromCacheByUniqueValue(String key, String languageId)
    {
        loadCache();
        if (key == null)
        {
            return null;
        }
        return replaceI18nFieldValueWithResource(cachedEntitiesByUniqueField.get(key), languageId);
    }

    protected List<T> fetchFromCacheByUniqueValuePattern(String keyPattern, String languageId)
    {
        loadCache();
        List<T> res = new ArrayList<T>();
        for (String key : cachedEntitiesByUniqueField.keySet())
        {
            if (key.contains(keyPattern))
            {
                if(!res.contains(cachedEntitiesByUniqueField.get(key)))
                {
                    res.add(cachedEntitiesByUniqueField.get(key));
                }
            }
        }
        return replaceI18nFieldValueWithResource(res, languageId);
    }

    private void saveUniqueFieldCache(T entity)
    {
        if (uniqueFields == null || uniqueFields.length == 0)
        {
            return;
        }
        String key = "";
        for (int i = 0; i < uniqueFields.length; i++)
        {
            Field uniqueField = uniqueFields[i];
            Object o = FieldValueUtil.fetchFieldValue(entity, uniqueField, logger);
            if (o == null)
            {
                key = "";
                break;
            }
            key = key + o + "-";
        }
        if (key.length() > 0)
        {
            if (cachedEntitiesByUniqueField.containsKey(key))
            {
                cachedEntitiesByUniqueField.replace(key, entity);
            }
            else
            {
                cachedEntitiesByUniqueField.put(key, entity);
            }
        }
    }

    private void saveCache(T record)
    {
        if (record == null || record.getId() == null)
        {
            return;
        }
        if (cachedEntitiesById.containsKey(record.getId()))
        {
            cachedEntitiesById.replace(record.getId(), record);
        }
        else
        {
            cachedEntitiesById.put(record.getId(), record);
        }
        saveUniqueFieldCache(record);
    }
}
