package com.polarj.model.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.polarj.common.CommonConstant;
import com.polarj.common.utility.FieldValueUtil;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.model.FieldSpecification;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.HasFieldInJson;
import com.polarj.model.HasSubWithFieldInJson;
import com.polarj.model.I18nResource;
import com.polarj.model.ModelFilterStrategy;
import com.polarj.model.ModelFilterStrategyItem;
import com.polarj.model.ModelSpecification;
import com.polarj.model.UserAccount;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.enumeration.FilterOperator;
import com.polarj.model.service.EntityService;
import com.polarj.model.service.FieldSpecificationService;
import com.polarj.model.service.I18nResourceService;
import com.polarj.model.service.ModelFilterStrategyService;
import com.polarj.model.service.ModelSpecificationService;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.Getter;
import lombok.Setter;

public class EntityServiceImpl<T extends GenericDbInfo, ID extends Serializable> implements EntityService<T, ID>
{
    private final int readDataDeepth = 4;

    @Autowired
    private @Getter @Setter JpaRepository<T, ID> repos;

    @Autowired
    @Lazy
    private I18nResourceService i18nService;

    @Autowired
    @Lazy
    private FieldSpecificationService fieldSpecService;

    @Autowired
    @Lazy
    private ModelSpecificationService modelSpecService;

    @Autowired
    @Lazy
    private ModelFilterStrategyService modelFilterStrategyService;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    @Autowired(required = false)
    @Qualifier(value = "wfEntityManager")
    private EntityManager wfEntityManager;

    @Override
    public T update(ID entityId, T entityWithUpdatedInfo, Integer operId, String languageId)
    {
        T res = null;
        if (entityWithUpdatedInfo == null || entityWithUpdatedInfo.getId() == null || entityWithUpdatedInfo.getId() == 0
                || !entityWithUpdatedInfo.getId().equals(entityId))
        {
            logger.info("the updated entity information is incorrect.");
            return res;
        }
        T existingEntity = getById(entityId, readDataDeepth, languageId);
        if (existingEntity == null)
        {
            logger.info("No entity found at this moment.");
            return res;
        }
        res = save(existingEntity, entityWithUpdatedInfo, operId, languageId);
        try
        {
            // 增加一个参数，填入false，不用每次更新都取出i18nsourceList到前端
            res = replaceI18nFieldValueWithResource(dataClone(res), languageId, Boolean.FALSE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    protected <ELM extends GenericDbInfo> void addElmToListField(ID id, String elmName, ELM newElm)
    {
        List<ELM> newElms = new ArrayList<ELM>();
        newElms.add(newElm);
        updateListField(id, elmName, newElms, false);
    }

    @SuppressWarnings("unchecked")
    private <ELM extends GenericDbInfo> void updateListField(ID id, String elmName, List<ELM> newElms,
            boolean clearExisting)
    {
        if (newElms == null)
        {
            return;
        }
        T entity = repos.getOne(id);
        if (null == entity)
        {
            return;
        }
        List<ELM> existings = null;
        try
        {
            Field listElm = FieldValueUtil.getTheField(entity.getClass(), elmName, logger);
            listElm.setAccessible(true);
            existings = (List<ELM>) listElm.get(entity);
            if (clearExisting)
            {
                existings.clear();
            }
            existings.addAll(newElms);
            repos.save(entity);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            existings = null;
        }
    }

    protected <ELM extends GenericDbInfo> void updateListField(ID id, String elmName, List<ELM> newElms)
    {
        updateListField(id, elmName, newElms, true);
    }

    protected <ELM extends GenericDbInfo> ELM fetchField(ID id, String fieldName)
    {
        T entity = repos.getOne(id);
        if (null == entity)
        {
            logger.info("Can not find entity with id: " + id + " using repos: " + repos.getClass().getSimpleName());
            return null;
        }
        ELM fieldValue = FieldValueUtil.fetchFieldValue(entity, fieldName, logger);
        return dataClone(fieldValue);
    }

    @Override
    public Page<T> list(Pageable p, String languageId)
    {
        if (repos instanceof PagingAndSortingRepository)
        {
            Page<T> res = ((PagingAndSortingRepository<T, ID>) repos).findAll(p);
            List<T> contents = res.getContent();
            List<T> newContents = (List<T>) replaceI18nFieldValueWithResource(dataClone(contents), languageId);
            Page<T> pRes = new PageImpl<>(newContents, p, res.getTotalElements());
            return pRes;
        }
        else
        {
            logger.info("This type " + repos.getClass().getSimpleName() + "of repository does not support paging.");
        }
        return null;
    }

    @Override
    public long count()
    {
        long total = repos.count();
        return total;
    }

    @Override
    public void delete(ID id, Integer operId)
    {
        if (id != null)
        {
            removeEntityRelatedI18nResources(id, operId);
            repos.deleteById(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(List<T> records, Integer operId)
    {
        if (records != null && records.size() != 0)
        {
            for (T record : records)
            {
                removeEntityRelatedI18nResources((ID) record.getId(), operId);
            }
            repos.deleteAll(records);
        }
    }

    private void removeEntityRelatedI18nResources(ID id, Integer operId)
    {
        T entity = getById(id, readDataDeepth, CommonConstant.defaultSystemLanguage);
        removeEntityResources(entity, operId);
        removeEntityFieldsResources(entity, operId);
    }

    private void removeEntityResources(GenericDbInfo entity, Integer operId)
    {
        if (entity == null)
        {
            return;
        }
        List<I18nResource> entityResources = entity.getI18nResources();
        if (entityResources != null && entityResources.size() > 0)
        {
            i18nService.delete(entityResources, operId);
        }
    }

    private void removeEntityFieldsResources(T entity, Integer operId)
    {
        Field[] fields = FieldValueUtil.getAllFields(entity.getClass(), logger);
        for (Field f : fields)
        {
            f.setAccessible(true);
            Object v = null;
            try
            {
                v = f.get(entity);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
            if (v == null)
            {
                continue;
            }
            FieldMetaData fm = f.getAnnotation(FieldMetaData.class);
            if (fm == null)
            {
                continue;
            }
            if (f.getType().isAnnotationPresent(Entity.class))
            {
                if (!fm.managementSeparately())
                {
                    removeEntityResources((GenericDbInfo) v, operId);
                }
            }
            else if (f.getType().isAssignableFrom(List.class))
            {
                // 这个属性是集合类型
                if (fm.managementSeparately())
                {
                    // 独立管理的业务模型属性，不用操作国际化数据
                    continue;
                }
                List<?> es = (List<?>) v;
                if (es.size() == 0)
                {
                    // 没有数据，那就不用处理了
                    continue;
                }
                Class<?> genericClazz = FieldValueUtil.getTypeOfField(f, logger);
                if (genericClazz == null || !GenericDbInfo.class.isAssignableFrom(genericClazz))
                {
                    // 这个类不是持久化基类的子类，那就不用处理了
                    continue;
                }
                @SuppressWarnings("unchecked")
                List<GenericDbInfo> newEs = (List<GenericDbInfo>) es;
                for (GenericDbInfo e : newEs)
                {
                    removeEntityResources(e, operId);
                }
            }
        }
    }

    @Override
    public boolean exists(ID id)
    {
        boolean exist = repos.existsById(id);
        return exist;
    }

    @Override
    public List<T> list(int limit, String languageId)
    {
        List<T> records = (List<T>) repos.findAll();

        return replaceI18nFieldValueWithResource(dataClone(records), languageId);
    }

    @Override
    public List<T> list(String languageId)
    {
        List<T> records = (List<T>) repos.findAll();

        return replaceI18nFieldValueWithResource(dataClone(records), languageId);
    }

    private <R extends GenericDbInfo> R dataClone(R record, int level)
    {
        if (record == null)
        {
            return null;
        }
        R r = null;
        try
        {
            r = record.deepClone(level, logger);
        }
        catch (Exception e)
        {
            r = null;
            logger.error(e.getMessage(), e);
        }
        return r;
    }

    protected <R extends GenericDbInfo> R dataClone(R record)
    {
        return dataClone(record, readDataDeepth);
    }

    private <R extends GenericDbInfo> Page<R> dataClone(Page<R> page, Pageable pageable, long totalRecords, int level)
    {
        List<R> content = dataClone(page.getContent(), level);
        PageImpl<R> res = new PageImpl<>(content, pageable, page.getTotalElements());
        return res;
    }

    protected <R extends GenericDbInfo> Page<R> dataClone(Page<R> page, Pageable pageable, long totalRecords)
    {
        return dataClone(page, pageable, totalRecords, readDataDeepth);
    }

    private <R extends GenericDbInfo> List<R> dataClone(List<R> records, int level)
    {
        List<R> recs = new ArrayList<R>();
        for (R rec : records)
        {
            R r = dataClone(rec, level);
            if (r != null)
            {
                recs.add(r);
            }
        }
        // 当records是不可修改的List时，这里会报错，但是不记得为什么要这么写了。
        // records.clear();
        // records.addAll(recs);
        return recs;
    }

    protected <R extends GenericDbInfo> List<R> dataClone(List<R> records)
    {
        return dataClone(records, readDataDeepth);
    }

    @Override
    public List<T> list(List<ID> ids, String languageId)
    {
        List<T> records = (List<T>) repos.findAllById(ids);
        return replaceI18nFieldValueWithResource(dataClone(records), languageId);
    }

    protected List<T> replaceI18nFieldValueWithResource(List<T> entities, String languageId)
    {
        if (entities == null || entities.size() == 0)
        {
            return entities;
        }
        T ent = entities.get(0);
        if (ent.getClass().equals(I18nResource.class))
        {
            return entities;
        }
        Field[] fields = FieldValueUtil.getAllFields(entities.get(0).getClass(), logger);
        if (fields == null || fields.length == 0)
        {
            logger.error("Get field of entity class {} error.", entities.get(0).getClass());
            return entities;
        }
        List<T> results = new ArrayList<T>();
        for (T entity : entities)
        {
            T res = replaceI18nFieldValueWithResource(entity, fields, languageId, Boolean.FALSE);
            if (res != null)
            {
                results.add(res);
            }
        }
        return results;
    }

    protected void doSomethingBeforeReturn(T entity)
    {

    }

    // 判断是否传输i18source是否需要传输到前端
    // 在编辑数据的时候使用,获取子类LISTD的i18数据
    protected List<T> replaceI18nFieldValueWithResource(List<T> entities, String languageId,
            Boolean needAddI18nResource)
    {
        if (entities == null || entities.size() == 0)
        {
            return entities;
        }
        T ent = entities.get(0);
        if (ent.getClass().equals(I18nResource.class))
        {
            return entities;
        }
        Field[] fields = FieldValueUtil.getAllFields(entities.get(0).getClass(), logger);
        if (fields == null || fields.length == 0)
        {
            logger.error("Get field of entity class {} error.", entities.get(0).getClass());
            return entities;
        }
        List<T> results = new ArrayList<T>();
        for (T entity : entities)
        {
            T res = replaceI18nFieldValueWithResource(entity, fields, languageId, needAddI18nResource);
            if (res != null)
            {
                results.add(res);
            }
        }
        return results;
    }

    // 在原方法上加一个参数，判断是否传输i18source是否需要传输到前端
    // 当获取list数据时，传入false，不传输i18数据，获取单条明细数据时，获取i18数据，在编辑数据的时候使用
    @SuppressWarnings("unchecked")
    private <D extends HasSubWithFieldInJson> T replaceI18nFieldValueWithResource(T entity, Field[] fields, String languageId,
            Boolean needAddI18nResource)
    {
        if (entity == null || fields == null || fields.length == 0)
        {
            return entity;
        }
        String i18nKeyFieldValue = null;
        try
        {
            i18nKeyFieldValue = fetchI18nKeyField(fields, entity);
        }
        catch (Exception e)
        {
            i18nKeyFieldValue = null;
            logger.error(e.getMessage(), e);
        }

        for (Field f : fields)
        {
            f.setAccessible(true);
            Object o = null;
            try
            {
                o = f.get(entity);
            }
            catch (Exception e)
            {
                o = null;
                logger.error(e.getMessage(), e);
            }
            if (f.isAnnotationPresent(I18nField.class))
            {
                handleReadingPrimitiveField(f, i18nKeyFieldValue, entity, languageId, needAddI18nResource);
            }
            else if (f.getType().isAnnotationPresent(Entity.class))
            {
                // 这个属性是一个持久化的属性， 需要处理其内部的国际化
                T v = (T) o;
                v = replaceI18nFieldValueWithResource(v, languageId, needAddI18nResource);
            }
            else if (f.getType().isAssignableFrom(List.class))
            {
                // 这个属性是集合类型
                // 如果这个集合类型的元素是一个需要持久话的模型，那么也需要处理其中的国际化属性
                List<T> es = (List<T>) o;
                if (es == null || es.size() == 0)
                {
                    // 没有数据，那就不用处理了
                    continue;
                }
                Class<?> genericClazz = FieldValueUtil.getTypeOfField(f, logger);
                if (genericClazz == null || !GenericDbInfo.class.isAssignableFrom(genericClazz))
                {
                    // 这个类不是持久化基类的子类，那就不用处理了
                    continue;
                }
                replaceI18nFieldValueWithResource(es, languageId, needAddI18nResource);
            }
        }
        doSomethingBeforeReturn(entity);
        if (HasSubWithFieldInJson.class.isAssignableFrom(entity.getClass()))
        {
            HasSubWithFieldInJson modelWithJsonField = (HasSubWithFieldInJson) entity;
            if (StringUtils.isNotEmpty(modelWithJsonField.getSubClassName()))
            {
                Class<D> subClazz;
                try
                {
                    subClazz = (Class<D>) Class.forName(modelWithJsonField.getSubClassName());
                    entity = (T) modelWithJsonField.castTo(subClazz);
                }
                catch (ClassNotFoundException e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return entity;
    }

    private void handleReadingPrimitiveField(Field f, String i18nKeyFieldValue, T entity, String languageId,
            Boolean needAddI18nResource)
    {
        String i18nKey = "";
        FieldMetaData i18nFieldMetaData = f.getAnnotation(FieldMetaData.class);
        if (i18nFieldMetaData == null)
        {
            // 说明这是一个没有被FieldMetaData注释的属性，直接返回即可。
            return;
        }
        // QUES: 在增加一个新功能之后，在执行与功能相关的操作就会i18nKeyFieldValue==null
        // 不知道原因是什么？
        if (i18nKeyFieldValue == null)
        {
            if (!Enum.class.isAssignableFrom(i18nFieldMetaData.enumClass()))
            {
                logger.error("No i18nKey field found for {}.{}!", f.getDeclaringClass().getName(), f.getName());
            }
            i18nKeyFieldValue = "";
        }
        if (i18nFieldMetaData.enumClass().equals(Object.class))
        {
            // 针对非枚举型数据
            i18nKey = f.getDeclaringClass().getName() + "." + f.getName() + "." + i18nKeyFieldValue.replace(' ', '-');
        }
        else
        {
            // 这是一个枚举类型的字段，有两种情况，一种是该属性是enum的类型，一种是普通类型（普通类型在复杂数据中处理）
            if (Enum.class.isAssignableFrom(i18nFieldMetaData.enumClass()))
            {
                // 针对枚举型数据，数据库保存的一定是枚举型数据值
                try
                {
                    i18nKey = i18nFieldMetaData.enumClass().getName() + "." + f.get(entity);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        if (StringUtils.isNotBlank(i18nKey))
        {
            setI18nFieldValue(f, i18nKey, entity, languageId, needAddI18nResource);
        }
    }

    private <M extends GenericDbInfo> boolean setI18nFieldValue(Field field, String i18nKey, M entity,
            String languageId, Boolean needAddI18nResource)
    {
        List<I18nResource> resources = i18nService.fetchI18nResourceBy(i18nKey);
        if (resources == null || resources.size() == 0)
        {
            return false;
        }
        boolean hasResource = false;
        field.setAccessible(true);
        for (I18nResource existingResource : resources)
        {
            if (needAddI18nResource != null && needAddI18nResource)
            {
                entity.addI18nResource(existingResource);
            }
            if (existingResource.getLanguageId().equals(languageId))
            {
                // 如果找到对应语言的版本，就使用该语言的版本。
                hasResource = true;
                try
                {
                    field.set(entity, existingResource.getI18nValue());
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        if (hasResource)
        {
            return hasResource;
        }
        for (I18nResource existingResource : resources)
        {
            // 如果找不到指定语言的版本，尝试找系统缺省语言的版本
            if (existingResource.getLanguageId().equals(CommonConstant.defaultSystemLanguage))
            {
                hasResource = true;
                try
                {
                    field.set(entity, existingResource.getI18nValue());
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                break;
            }
        }
        return hasResource;
    }

    // msg （处理i18大量数据是否传入后台）因为list和getId单条数据使用的都是同一个方法，。。。
    // 所以加了一个参数的形参方法，判断是否传输i18source是否需要传输到前端
    // 当获取list数据时，传入false，不传输i18数据，获取单条明细数据时，获取i18数据，在编辑数据的时候使用
    protected T replaceI18nFieldValueWithResource(T entity, String languageId, Boolean needAddI18nResource)
    {
        if (entity == null)
        {
            return entity;
        }
        Field[] fields = FieldValueUtil.getAllFields(entity.getClass(), logger);
        return replaceI18nFieldValueWithResource(entity, fields, languageId, needAddI18nResource);
    }

    // 修改原方法默认为needAddI18nResource = true
    protected T replaceI18nFieldValueWithResource(T entity, String languageId)
    {
        if (entity == null)
        {
            return entity;
        }
        Field[] fields = FieldValueUtil.getAllFields(entity.getClass(), logger);
        return replaceI18nFieldValueWithResource(entity, fields, languageId, Boolean.TRUE);
    }

    @Override
    public T getById(ID id, String languageId)
    {
        return getById(id, readDataDeepth, languageId);
    }

    @Override
    public T getById(ID id, int deepth, String languageId)
    {
        T record = repos.getOne(id);
        return replaceI18nFieldValueWithResource(dataClone(record, deepth), languageId, Boolean.TRUE);
    }

    private boolean hasI18nField(Field[] fields) throws Exception
    {
        for (Field f : fields)
        {
            if (f.isAnnotationPresent(I18nField.class))
            {
                FieldMetaData fmd = f.getAnnotation(FieldMetaData.class);
                if (fmd == null || Enum.class.isAssignableFrom(fmd.enumClass()))
                {
                    // 这是一个没有被FieldMetaData注释的属性，或者这个属性是一个简单枚举型数据
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    private String fetchI18nKeyField(Field[] fields, GenericDbInfo entity) throws Exception
    {
        String i18nKeyFieldValue = null;

        for (Field f : fields)
        {
            if (f.isAnnotationPresent(I18nKeyField.class))
            {
                f.setAccessible(true);
                i18nKeyFieldValue = (String) f.get(entity);
                break;
            }
        }
        return i18nKeyFieldValue;
    }

    // 在新增数据的情况下，oldEntity是为null的
    @SuppressWarnings("unchecked")
    private void saveUpdatedI18nFieldsValue(GenericDbInfo oldEntity, GenericDbInfo newEntity, Integer operId,
            String languageId) throws Exception
    {
        Field[] fields = FieldValueUtil.getAllFields(newEntity.getClass(), logger);
        // 找到I18nKeyField
        String i18nKeyFieldValue = fetchI18nKeyField(fields, newEntity);
        boolean hasI18nField = hasI18nField(fields);
        // 如果一个业务模型有需要国际化的属性（同时这个属性不是一个普通的枚举）
        // 但是没有用于资源key的值，这应该是一个错误
        // QUES： 是不是在代码检查的时候要发现这个错误啊？
        if (hasI18nField && (i18nKeyFieldValue == null || i18nKeyFieldValue.length() == 0))
        {
            logger.error("This model " + newEntity.getClass().getSimpleName()
                    + " does not have field annotated with I18nKeyField or the value of the field is empty.");
            return;
        }
        for (Field f : fields)
        {
            if (f.isAnnotationPresent(Transient.class))
            {
                continue;
            }
            f.setAccessible(true);
            FieldMetaData fm = f.getAnnotation(FieldMetaData.class);
            Object v = f.get(newEntity);
            if (v == null && !f.isAnnotationPresent(I18nField.class))
            {// 這個屬性沒有值， 就不用處理了。
                continue;
            }
            if (f.isAnnotationPresent(I18nField.class))
            {
                // 处理有国际化注解的字段，不仅修改字段的值，同时把值保存到资源表中
                // 根据设计要求，缺省的数据值是系统缺省语言对应的值， 所以，如果不是缺省系统语言，
                // 要确保不能更新缺省数据
                FieldMetaData i18nFieldMetaData = f.getAnnotation(FieldMetaData.class);
                if (i18nFieldMetaData != null && i18nFieldMetaData.enumClass().equals(Object.class))
                {
                    handleSavingNormalField(f, i18nKeyFieldValue, oldEntity, newEntity, operId, languageId);
                }
                /*
                 * else { // QUES: 这里好像有问题，暂时注释掉 //
                 * 枚举类型字段中存储的应该是枚举值，而不是label，所以无需做枚举类型数据的国际化保存 //
                 * 枚举类型的国际化只应该资源化修改。 String enumClassName =
                 * i18nField.enumClass().getName(); handleSavingEnumField(f,
                 * newEntity, enumClassName); } //
                 */
            }
            else if (f.getType().isAnnotationPresent(Entity.class))
            {
                // 如果这个属性也是一个需要持久化的模型， 那么也需要处理其中的国际化属性
                GenericDbInfo oldValue = null;
                GenericDbInfo newValue = (GenericDbInfo) v;
                if (oldEntity != null)
                {
                    oldValue = (GenericDbInfo) f.get(oldEntity);
                }
                if (fm != null && !fm.managementSeparately())
                {
                    saveUpdatedI18nFieldsValue(oldValue, newValue, operId, languageId);
                }
            }
            else if (f.getType().isAssignableFrom(List.class))
            {
                // 这个属性是集合类型
                // 如果这个集合类型的元素是一个需要持久话的模型，那么也需要处理其中的国际化属性
                List<?> es = (List<?>) v;
                if (es.size() == 0)
                {
                    // 没有数据，那就不用处理了
                    continue;
                }
                Class<?> genericClazz = FieldValueUtil.getTypeOfField(f, logger);
                if (genericClazz == null || !GenericDbInfo.class.isAssignableFrom(genericClazz))
                {
                    // 这个类不是持久化基类的子类，那就不用处理了
                    continue;
                }
                List<GenericDbInfo> newEs = (List<GenericDbInfo>) es;
                List<GenericDbInfo> oldEs = null;
                if (oldEntity != null)
                {
                    oldEs = (List<GenericDbInfo>) f.get(oldEntity);
                }
                for (GenericDbInfo e : newEs)
                {
                    if (oldEs == null || oldEs.size() == 0 || e.getId() == null || e.getId() == 0)
                    {
                        saveUpdatedI18nFieldsValue(null, e, operId, languageId);
                    }
                    else
                    {
                        for (GenericDbInfo oe : oldEs)
                        {
                            if (oe.getId().intValue() == e.getId().intValue())
                            {
                                if (fm != null && !fm.managementSeparately())
                                {
                                    saveUpdatedI18nFieldsValue(oe, e, operId, languageId);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleSavingNormalField(Field f, String i18nKeyFieldValue, GenericDbInfo oldEntity,
            GenericDbInfo newEntity, Integer operId, String languageId) throws Exception
    {
        // 针对非枚举型字段的处理过程
        String i18nKey = f.getDeclaringClass().getName() + "." + f.getName() + "."
                + i18nKeyFieldValue.replace(' ', '-');

        List<I18nResource> newResources = newEntity.getI18nResources();
        if (newResources != null)
        {
            for (I18nResource newResource : newResources)
            {
                if (i18nKey.equals(newResource.getI18nKey()) && newResource.valid())
                {
                    I18nResource existingResource = i18nService.fetchI18nResource(i18nKey, newResource.getLanguageId());
                    if (existingResource == null)
                    {
                        i18nService.create(newResource, operId, newResource.getLanguageId());
                    }
                    else
                    {
                        existingResource.setI18nValue(newResource.getI18nValue());
                        i18nService.update(existingResource.getId(), existingResource, operId,
                                newResource.getLanguageId());
                    }
                }
            }
        }
        else
        {
            String v = (String) f.get(newEntity);
            if (v != null && v.length() > 0)
            {
                I18nResource existingResource = i18nService.fetchI18nResource(i18nKey, languageId);
                if (existingResource == null)
                {
                    I18nResource nRes = new I18nResource();
                    nRes.setCreatedBy(operId);
                    // nRes.setCreatedDate(new Date());
                    nRes.setI18nKey(i18nKey);
                    nRes.setLanguageId(languageId);
                    nRes.setI18nValue(v);
                    i18nService.create(nRes, operId, languageId);
                }
                else
                {
                    existingResource.setI18nValue(v);
                    i18nService.update(existingResource.getId(), existingResource, operId, languageId);
                }
            }
        }
        if (!CommonConstant.defaultSystemLanguage.equals(languageId))
        {
            // 如果操作的语言不是缺省的系统语言， 恢复 "需要更新的数据库的值" 到系统缺省语言版本
            // 修复获取matedata数据时，表头字段语言读取问题
            // 保存数据语言为操作语言，如果没有则保存为缺省语言，如果再没有，则保存为null

            // ques这里有个问题，这个update方法，去mteadata的时候，需要取回最新的对应的操作语言数据的
            I18nResource i18Res = i18nService.fetchI18nResource(i18nKey, languageId);
            if (i18Res != null)
            {
                f.set(newEntity, i18Res.getI18nValue());
            }
            else
            {
                i18Res = i18nService.fetchI18nResource(i18nKey, CommonConstant.defaultSystemLanguage);
                if (i18Res != null)
                {
                    f.set(newEntity, null);
                }
            }
            // if (oldEntity != null)
            // {
            // f.set(newEntity, f.get(oldEntity));
            // }
            // else
            // {
            // f.set(newEntity, null);
            // }
        }
    }

    @SuppressWarnings("unchecked")
    private <S extends HasFieldInJson, D extends S, M extends GenericDbInfo> D saveModelWithChangeableField(D entity,
            Integer operId, String languageId)
    {
        if (HasSubWithFieldInJson.class.isAssignableFrom(entity.getClass()))
        {
            ((HasSubWithFieldInJson) entity).setSubClassName(entity.getClass().getName());
        }
        entity.getJsonFields();
        Class<?> clazz = entity.getClass();
        Class<D> inClazz = (Class<D>) entity.getClass();
        while (!clazz.isAnnotationPresent(Table.class))
        {
            clazz = clazz.getSuperclass();
        }
        try
        {
            T anEntity = (T) clazz.newInstance();
            Field[] fields = FieldValueUtil.getAllFields(clazz, logger);
            for (Field f : fields)
            {
                if (Modifier.isFinal(f.getModifiers()))
                {
                    continue;
                }
                f.setAccessible(true);
                if (f.get(entity) != null)
                {
                    if (f.getType().isAssignableFrom(List.class))
                    {
                        List<M> ids = (List<M>) f.get(entity);
                        for (M m : ids)
                        {
                            m.setCreatedBy(operId);
                            m.setCreatedDate(new Date());
                        }
                    }
                    else if (f.getType().isAnnotationPresent(Entity.class))
                    {
                        M m = (M) f.get(entity);
                        m.setCreatedBy(operId);
                        m.setCreatedDate(new Date());
                    }
                    f.set(anEntity, f.get(entity));
                }
            }
            anEntity.setCreatedBy(operId);
            anEntity.setCreatedDate(new Date());
            T res = repos.save(anEntity);
            D m = ((HasFieldInJson) res).castTo(inClazz);
            return m;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // 在新增数据的情况下，oldEntity是为null的
    private T save(T oldEntity, T newEntity, Integer operId, String languageId)
    {
        T res = null;
        try
        {
            // if (!CommonConstant.defaultSystemLanguage.equals(languageId))
            {// 只有在使用的语言不是系统缺省语言的情况下，才需要保存国际化的属性字段
                saveUpdatedI18nFieldsValue(oldEntity, newEntity, operId, languageId);
            }
        }
        catch (Exception e)
        {
            logger.error("saveAllI18nFieldValues has error: " + e.getMessage(), e);
        }
        if (newEntity instanceof HasFieldInJson)
        {
            res = (T) saveModelWithChangeableField((HasFieldInJson) newEntity, operId, languageId);
        }
        else
        {
            T willBeSavedEntity = null;
            if (oldEntity == null)
            {
                willBeSavedEntity = newEntity;
            }
            else
            {
                try
                {
                    copyData(oldEntity, newEntity, operId);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                willBeSavedEntity = oldEntity;
            }
            res = repos.save(willBeSavedEntity);
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    private <M extends GenericDbInfo> void updateChildElms(Field f, List<M> oldValues, List<M> newValues,
            boolean updateValue, Integer operId) throws Exception
    {
        List<M> willBeAddedElms = new ArrayList<M>();
        List<M> willBeRemovedElms = new ArrayList<M>();
        for (M newV : newValues)
        {
            boolean hasElm = false;
            for (M oldV : oldValues)
            {
                if (oldV.getId().equals(newV.getId()))
                {
                    hasElm = true;
                    if (updateValue)
                    {
                        copyData((T) oldV, (T) newV, operId);
                    }
                    break;
                }
            }
            if (!hasElm)
            {
                newV.setCreatedBy(operId);
                newV.setCreatedDate(new Date());
                willBeAddedElms.add(newV);
            }
        }

        for (M oldV : oldValues)
        {
            boolean hasElm = false;
            for (M newV : newValues)
            {
                if (oldV.getId().equals(newV.getId()))
                {
                    hasElm = true;
                }
            }
            if (!hasElm)
            {
                willBeRemovedElms.add(oldV);
            }
        }
        if (willBeAddedElms.size() > 0)
        {
            oldValues.addAll(willBeAddedElms);
        }
        if (willBeRemovedElms.size() > 0)
        {
            oldValues.removeAll(willBeRemovedElms);
            for (M elm : willBeRemovedElms)
            {
                removeEntityResources(elm, operId);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <M extends GenericDbInfo> void copyData(T dest, T sor, Integer operId) throws Exception
    {
        Field[] fields = FieldValueUtil.getAllFields(sor.getClass(), logger);
        for (Field f : fields)
        {
            if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
            {
                continue;
            }
            if (f.isAnnotationPresent(Transient.class))
            {
                // 非持久性属性，就不需要管了。
                continue;
            }
            f.setAccessible(true);
            Object v = f.get(sor);
            Class<?> genericClazz = null;
            if (v == null)
            {// 這個屬性沒有值， 就不用處理了。
             // 这是一段规避hibernate错误的代码：https://hibernate.atlassian.net/browse/HHH-9940
                if (f.getType().isAssignableFrom(List.class) && f.get(dest) == null)
                {
                    genericClazz = FieldValueUtil.getTypeOfField(f, logger);
                    if (genericClazz != null && GenericDbInfo.class.isAssignableFrom(genericClazz))
                    {
                        f.set(dest, new ArrayList<GenericDbInfo>());
                    }
                }
                // 这是一段规避hibernate错误的代码
                continue;
            }
            FieldMetaData fm = f.getAnnotation(FieldMetaData.class);
            if (f.getType().isAssignableFrom(List.class))
            {
                List<M> oldSubs = (List<M>) f.get(dest);
                if (oldSubs == null)
                {
                    oldSubs = new ArrayList<M>();
                    f.set(dest, oldSubs);
                }
                genericClazz = (genericClazz == null ? FieldValueUtil.getTypeOfField(f, logger) : genericClazz);
                if (genericClazz == null || !GenericDbInfo.class.isAssignableFrom(genericClazz))
                {
                    // 这个类不是持久化基类的子类，直接使用值就好了，这个情况应该不会出现的
                    f.set(dest, v);
                    continue;
                }
                if (fm != null && fm.managementSeparately())
                {
                    // 对于一对多关系中是独立管理的属性类，我们只关心有无，并不修改值
                    Class<M> entityClass = (Class<M>) genericClazz;
                    List<M> subElms = new ArrayList<M>();
                    List<M> ids = (List<M>) v;
                    EntityService<M, Integer> subService = SpringContextUtils.getModelServiceBean(entityClass);
                    for (M id : ids)
                    {
                        M m = subService.getById(id.getId(), CommonConstant.defaultSystemLanguage);
                        if (m != null)
                        {
                            subElms.add(m);
                        }
                    }
                    updateChildElms(f, oldSubs, subElms, false, operId);
                }
                else
                {
                    // 对于一对多关系中是“不是”独立管理的属性类，我们不仅关心有无，还需要关注被修改的值
                    updateChildElms(f, oldSubs, (List<M>) v, true, operId);
                }
            }
            else if (f.getType().isAnnotationPresent(Entity.class))
            {
                if (fm != null && fm.managementSeparately())
                {
                    genericClazz = FieldValueUtil.getTypeOfField(f, logger);
                    if (genericClazz == null || !GenericDbInfo.class.isAssignableFrom(genericClazz))
                    {
                        // 这个类不是持久化基类的子类，那就不用处理了
                        continue;
                    }
                    Class<M> entityClass = (Class<M>) genericClazz;
                    EntityService<M, Integer> subService = SpringContextUtils.getModelServiceBean(entityClass);
                    M id = (M) v;
                    M m = subService.getById(id.getId(), CommonConstant.defaultSystemLanguage);
                    f.set(dest, m);
                }
                else
                {
                    if (f.get(dest) != null)
                    {
                        copyData((T) f.get(dest), (T) f.get(sor), operId);
                    }
                    else
                    {
                        f.set(dest, v);
                    }
                }
            }
            else
            {
                f.set(dest, v);
            }
        }
    }

    @Override
    public T create(T entity, Integer operId, String languageId)
    {
        if (entity != null && entity.getId() == null)
        {
            entity.setCreatedBy(operId);
            entity.initDefaultValueBeforeCreation();
            // 如果手动新增也要保存校验错误的话，那么需要使用下面代码。现在先注释掉，只做前端校验后返回，不进行保存
            // validateEntity(entity);
            return save(null, entity, operId, languageId);
        }
        return null;
    }

    @Override
    public List<T> create(List<T> entities, Integer operId, String languageId)
    {
        if (entities == null || entities.size() == 0)
        {
            return null;
        }

        // QUES: 这个地方需要优化，目前是一条记录一条记录单独处理
        // 应该做批量处理
        List<T> newEntities = new ArrayList<T>();
        for (T entity : entities)
        {
            // 如果手动新增也要保存校验错误的话，那么校验方法需要放到create方法里面
            validateEntity(entity, operId, languageId);
            T newEntity = create(entity, operId, languageId);
            newEntities.add(newEntity);
        }
        return newEntities;
    }

    private List<FieldSpecification> getFieldMetaDataByAnnotation(Class<?> clazz, List<String> generatedClazzNames,
            UserAccount userAcc, String languageId)
    {
        // QUES： 这个方法被嵌套访问，暂时做两层就够？
        // 或者把已经获取过元数据的类名作为参数传递，在列表中的就不用再获取了？
        Field[] fs = FieldValueUtil.getAllFields(clazz, logger);
        if (fs == null || fs.length == 0)
        {
            return null;
        }
        List<FieldSpecification> fsfs = new ArrayList<>();
        for (Field f : fs)
        {
            FieldMetaData fMeta = f.getAnnotation(FieldMetaData.class);
            if (fMeta == null)
            {
                continue;
            }
            FieldSpecification fieldSpec = generateSpecificationByAnnotation(clazz.getName(), generatedClazzNames, f,
                    fMeta, userAcc, languageId);
            if (fieldSpec != null)
            {
                fsfs.add(fieldSpec);
            }
        }
        return fsfs;
    }

    // 自动生成的部分不能通过数据库修改， 其他的都可以通过数据库操作来修改
    // 因此，FieldMetaData注解描述的内容就是FieldSpecification的初始值
    // 如果在FieldSpecification中被描述为autogenerated=true的属性，直接使用
    // 初始值替换数据库中的值，否则就以数据库中的值为准。
    private FieldSpecification generateSpecificationByAnnotation(String className, List<String> generatedClazzNames,
            Field f, FieldMetaData fm, UserAccount userAcc, String languageId)
    {
        String fieldName = f.getName();
        FieldSpecification fs = FieldSpecification.generateFieldSpecificationByFieldMetaData(fm, className, fieldName);
        if (fs == null)
        {
            return null;
        }
        if (fs.getEnumFlag())
        {
            // 是枚举类型，需要把选择的数据放到FieldSpecification的selectiveValues属性中。
            List<String> selectiveValues = convertFrom(fm.enumClass(), fm.labelField(), fm.hasOwner(), userAcc,
                    languageId);
            if (selectiveValues != null && selectiveValues.size() > 0)
            {
                fs.setSelectiveValues(selectiveValues);
            }
            else
            {
                fs.setSelectiveValues(new ArrayList<String>());
            }
            // FIXEME: 如果没有得到selectiveValues的值该怎么办？
        }
        Field field = FieldValueUtil.getTheField(fs.getClass(), "label", logger);
        boolean hasLabel = setI18nFieldValue(field,
                (FieldSpecification.class.getName() + ".label." + fs.getFieldFullName()), fs, languageId,
                Boolean.FALSE);
        if (!hasLabel)
        {
            // 任何语言版本都没有，把系统设置的作为缺省语言版本保存到数据库
            I18nResource i18nResource = new I18nResource();
            i18nResource.setI18nKey(FieldSpecification.class.getName() + ".label." + fs.getFieldFullName());
            i18nResource.setI18nValue(fm.label());
            i18nResource.setLanguageId(CommonConstant.defaultSystemLanguage);
            i18nService.create(i18nResource, CommonConstant.systemUserAccountId, CommonConstant.defaultSystemLanguage);
        }
        I18nResource i18nResource = i18nService
                .fetchI18nResource(FieldSpecification.class.getName() + ".tip." + fs.getFieldFullName(), languageId);
        if (i18nResource != null)
        {
            fs.setTip(i18nResource.getI18nValue());
        }
        else
        {
            fs.setTip(fm.tip());
        }

        // 如果属性是Transient的，对于字符串类型的，必须指定FieldMetaData的最大长度
        Column c = f.getAnnotation(Column.class);
        if (c != null && f.getType().equals(String.class))
        {
            if (fm.maxLength() == 0 || fm.maxLength() > c.length())
            {
                fs.setMaxLength(c.length());
            }
        }

        Class<?> fieldType = FieldValueUtil.getTypeOfField(f, logger);
        if (!fm.managementSeparately() && fieldType.isAnnotationPresent(Entity.class)
                || fieldType.isAnnotationPresent(Embeddable.class))
        {
            if (!generatedClazzNames.contains(fieldType.getName()))
            {
                generatedClazzNames.add(fieldType.getName());
                List<FieldSpecification> fSpecs = getFieldMetaDataByAnnotation(fieldType, generatedClazzNames, userAcc,
                        languageId);
                if (fSpecs != null && fSpecs.size() > 0)
                {
                    fs.setComponentMetaDatas(fSpecs);
                }
            }
        }
        fs.setI18nField(Boolean.FALSE);
        if (f.isAnnotationPresent(I18nField.class))
        {
            fs.setI18nField(Boolean.TRUE);
        }
        if (f.isAnnotationPresent(I18nKeyField.class))
        {
            fs.setI18nKeyField(Boolean.TRUE);
        }
        return fs;
    }

    @SuppressWarnings("unchecked")
    private List<String> convertFrom(Class<?> clazz, String labelField, boolean hasOwner, UserAccount userAcc,
            String languageId)
    {
        if (Enum.class.isAssignableFrom(clazz))
        {// 对于枚举类型，直接获取枚举类型的数据
            List<String> values = new ArrayList<String>();
            Enum<?>[] allEnums = (Enum<?>[]) clazz.getEnumConstants();
            for (Enum<?> oneEnum : allEnums)
            {// FIXME: 需要从数据库中拿到国际化的值，没有，就直接用name。
                String i18nKey = clazz.getName() + "." + oneEnum.name();
                I18nResource i18nRes = i18nService.fetchI18nResource(i18nKey, languageId);
                if (i18nRes == null)
                {
                    i18nRes = i18nService.fetchI18nResource(i18nKey, CommonConstant.defaultSystemLanguage);
                }
                String v = null;
                if (i18nRes != null)
                {
                    v = oneEnum.name() + "," + i18nRes.getI18nValue();
                }
                else
                {
                    v = oneEnum.name() + "," + oneEnum.name();
                }
                values.add(v);
            }
            return values;
        }
        else if (GenericDbInfo.class.isAssignableFrom(clazz))
        {
            return convertFromEntityClass((Class<GenericDbInfo>) clazz, labelField, hasOwner, userAcc, languageId);
        }
        return null;
    }

    private <M extends GenericDbInfo> List<String> convertFromEntityClass(Class<M> clazz, String labelField,
            boolean hasOwner, UserAccount userAcc, String languageId)
    {

        EntityService<M, ID> service = SpringContextUtils.getModelServiceBean(clazz);

        List<M> res = null;
        if (hasOwner)
        {
            res = service.listByOwner(userAcc, languageId);
        }
        else
        {
            res = service.list(languageId);
        }

        List<String> idWithLabels = new ArrayList<String>();
        if (res == null || res.size() == 0)
        {
            return idWithLabels;
        }
        for (M m : res)
        {
            if (m != null)
            {
                String label = FieldValueUtil.fetchFieldValue(m, labelField, logger);
                idWithLabels.add(m.getId() + "," + label);
            }
        }

        return idWithLabels;
    }

    // 在方法被调用钱，entity已经判断过，是非空的
    protected Map<String, List<String>> listI18nKeyFieldValuesForI18nFieldSearching(T entity, String languageId)
    {
        // TODO
        // 读出模型中国际化字段的值，在资源表中查询出所有符合条件的数据
        // 符合条件的资源数据中， 每个数据的key是下面几种情况，我们需要搜索的应该是第3种情况
        // 1. 枚举类型：枚举类型全名+.+值名
        // 2. 模型属性作为报表头或者页面的label等：模型类全名+.+属性名
        // 3. 模型属性的值：模型类全名+.+属性名+.+注解了i18nkeyfield的模型属性的值

        // 搜索过程：
        // 1. 搜索所有i18nkey以模型类全名+.+需要国际化的属性名+.开头的，i18nvalue包含指定值的所有数据
        // 2. 把i18nkeyfield属性的值组成一个列表作为下一段查询的条件。
        Map<String, List<String>> i18nkeyFieldValues = new HashMap<String, List<String>>();
        // 搜索条件1：i18nkey以模型类全名+.+需要国际化的属性名+.开头的
        // 搜索条件2：i18nvalue包含所有出现的值
        // Map<String, String>: 前一个是搜索条件1,后一个是搜索条件2
        Map<String, String> i18nFieldValuePatterns = fetchI18nFieldValuePattern(entity);
        List<I18nResource> resources = listI18nResourceByKeyAndFieldValue(i18nFieldValuePatterns);
        if (resources == null || resources.size() == 0)
        {
            return i18nkeyFieldValues;
        }
        for (I18nResource resource : resources)
        {
            String i18nKey = resource.getI18nKey();
            String i18nKeyFieldValue = i18nKey.substring(i18nKey.lastIndexOf(".") + 1);
            String i18nFieldName = i18nKey.substring(0, i18nKey.lastIndexOf("."));
            String i18nClassName = i18nFieldName.substring(0, i18nFieldName.lastIndexOf("."));
            List<String> keyFieldValues = i18nkeyFieldValues.get(i18nClassName);
            if (keyFieldValues == null)
            {
                keyFieldValues = new ArrayList<String>();
                i18nkeyFieldValues.put(i18nClassName, keyFieldValues);
            }
            if (!keyFieldValues.contains(i18nKeyFieldValue))
            {
                keyFieldValues.add(i18nKeyFieldValue);
            }
        }
        return i18nkeyFieldValues;
    }

    // QUES: 如果有两个及以上的国际化属性，是and还是or条件来搜索资源库呢？
    // 得到所有含有相关国际化值的资源
    private List<I18nResource> listI18nResourceByKeyAndFieldValue(Map<String, String> i18nPatterns)
    {
        List<I18nResource> res = null;
        if (i18nPatterns != null && i18nPatterns.size() > 0)
        {
            PathBuilder<I18nResource> entityPath = new PathBuilder<I18nResource>(I18nResource.class, "resource");
            JPAQuery<I18nResource> query = (new JPAQuery<I18nResource>(entityManager)).from(entityPath);
            Predicate predicate = null;
            for (String keyPattern : i18nPatterns.keySet())
            {
                predicate = entityPath.getString("i18nKey").startsWith(keyPattern)
                        .and(entityPath.getString("i18nValue").contains(i18nPatterns.get(keyPattern))).or(predicate);
            }
            res = query.where(predicate).distinct().fetch();
        }
        return res;
    }

    private <M extends GenericDbInfo> String fetchI18nKeyFieldName(M entity)
    {
        Field[] fields = FieldValueUtil.getAllFields(entity.getClass(), logger);
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(I18nKeyField.class))
            {
                return field.getName();
            }
        }
        return null;
    }

    // 得到需要查找的国际化字段，与该国际化字段会包含值的MAP
    @SuppressWarnings("unchecked")
    private <M extends GenericDbInfo> Map<String, String> fetchI18nFieldValuePattern(M entity)
    {
        Map<String, String> i18nFieldValuePatterns = new HashMap<String, String>();
        String i18nKeyFieldName = fetchI18nKeyFieldName(entity);
        if (i18nKeyFieldName == null || i18nKeyFieldName.length() == 0)
        {// 如果找不到i18nkeyField，那么就不会有国际化资源
            return i18nFieldValuePatterns;
        }
        Field[] fields = FieldValueUtil.getAllFields(entity.getClass(), logger);

        for (Field field : fields)
        {
            Class<?> fieldType = FieldValueUtil.getTypeOfField(field, logger);
            if (fieldType.isAnnotationPresent(Entity.class) && !field.isAnnotationPresent(Transient.class))
            {
                Object obj = FieldValueUtil.fetchFieldValue(entity, field, logger);
                if (obj != null)
                {
                    M model = null;
                    if (List.class.isAssignableFrom(field.getType()))
                    {
                        if (((List<M>) obj).size() > 0)
                        {
                            model = ((List<M>) obj).get(0);
                        }
                    }
                    else
                    {
                        model = (M) obj;
                    }
                    if (model != null)
                    {
                        // 属性的值是很可能为空值的，所以需要排除空值的可能性
                        Map<String, String> compI18nFieldValuePatterns = fetchI18nFieldValuePattern(model);
                        if (compI18nFieldValuePatterns.size() > 0)
                        {
                            i18nFieldValuePatterns.putAll(compI18nFieldValuePatterns);
                        }
                    }
                }
            }
            if (field.isAnnotationPresent(I18nField.class))
            {
                String v = FieldValueUtil.fetchFieldValue(entity, field, logger);
                if (v != null && v.length() > 0)
                {
                    i18nFieldValuePatterns.put(entity.getClass().getName() + "." + field.getName(), v);
                }
            }
        }

        return i18nFieldValuePatterns;
    }

    @Override
    public Page<T> listByCriteria(T entity, String sortField, boolean desc, String languageId, Integer dataLevel)
    {
        if (entity == null)
        {
            return null;
        }
        Map<String, List<String>> i18nKeyFieldValues = listI18nKeyFieldValuesForI18nFieldSearching(entity, languageId);
        Page<T> res = null;
        if (StringUtils.isEmpty(entity.getSmartSearchText()))
        {
            res = listByCriteriaUsingQueryDsl(entity, i18nKeyFieldValues, sortField, desc, languageId, dataLevel);
        }
        else
        {
            // 采用快速查询里面的值查找数据，
            // 首先把需要快速查找的数据分配到各个属性中去
            // 然后使用或的条件组合来查找
            res = listByCriteriaUsingQueryDsl(entity, i18nKeyFieldValues, sortField, desc, languageId, dataLevel,
                    false);
            ;
        }
        return res;
    }

    // 这个方法是按照“与”的方案创建查询条件，并得到结果。
    private <M extends GenericDbInfo> Page<T> listByCriteriaUsingQueryDsl(T entity,
            Map<String, List<String>> i18nKeyFieldValues, String sortField, boolean desc, String languageId,
            Integer dataLevel)
    {
        return listByCriteriaUsingQueryDsl(entity, i18nKeyFieldValues, sortField, desc, languageId, dataLevel, true);
    }

    @SuppressWarnings("unchecked")
    private <M extends GenericDbInfo> Page<T> listByCriteriaUsingQueryDsl(T entity,
            Map<String, List<String>> i18nKeyFieldValues, String sortField, boolean desc, String languageId,
            Integer dataLevel, boolean andCondition)
    {
        EntityManager entityManagerUsed = entityManager;
        // QUES:这一段代码用于确定使用哪一个数据源，有没有简单的方式知道业务模型在哪个数据源的管理下呢？
        if (entity.getClass().getName().contains(".workflow."))
        {
            entityManagerUsed = wfEntityManager;
        }
        entityManagerUsed.clear();
        String alias = entity.getEntityName().toLowerCase();
        List<String> aliases = new ArrayList<String>();
        aliases.add(alias);
        PathBuilder<M> entityPath = new PathBuilder<M>((Class<M>) entity.getClass(), alias);
        JPAQuery<T> query = (new JPAQuery<T>(entityManagerUsed)).from(entityPath);
        Predicate predicate = mergePredicate(null, entityPath, (M) entity, i18nKeyFieldValues, andCondition);
        Field[] fields = FieldValueUtil.getAllFields(entity.getClass(), logger);
        for (Field field : fields)
        {
            Class<?> fieldType = FieldValueUtil.getTypeOfField(field, logger);
            if (fieldType.isAnnotationPresent(Entity.class) && !field.isAnnotationPresent(Transient.class))
            {
                alias = fieldType.getSimpleName().toLowerCase();
                while (aliases.contains(alias))
                {
                    alias = alias + "_";
                }
                aliases.add(alias);
                PathBuilder<M> path = new PathBuilder<M>((Class<M>) fieldType, alias);
                FieldMetaData fm = field.getAnnotation(FieldMetaData.class);
                predicate = mergePredicate(predicate, path, FieldValueUtil.fetchFieldValue(entity, field, logger),
                        i18nKeyFieldValues, andCondition);
                // 根据条件中是否有该属性的出现，判断是否需要做连接查询
                boolean needJoinThisField = ((predicate != null) && (predicate.toString().indexOf(alias) != -1));
                if (!needJoinThisField)
                {
                    // 不需要连接该属性查询，继续查找需要连接的属性。
                    continue;
                }
                if (fm != null && !fm.managementSeparately())
                {
                    if (field.getType().isAssignableFrom(List.class))
                    {
                        query = query.innerJoin(entityPath.getCollection(field.getName(), (Class<M>) fieldType), path);
                    }
                    else
                    {
                        query = query.innerJoin(entityPath.get(field.getName(), (Class<M>) fieldType), path);
                    }
                }
                else
                {
                    if (field.getType().isAssignableFrom(List.class))
                    {
                        query = query.leftJoin(entityPath.getCollection(field.getName(), (Class<M>) fieldType), path);
                    }
                    else
                    {
                        query = query.leftJoin(entityPath.get(field.getName(), (Class<M>) fieldType), path);
                    }
                }
            }
        }
        long total = query.where(predicate).distinct().fetchCount();
        if (total == 0)
        {
            return null;
        }
        long offset = (entity.getPageIndex() - 1) * entity.getCurPageSize();
        OrderSpecifier<String> o = null;
        if (sortField != null && sortField.length() > 0)
        {
            Order order = Order.ASC;
            if (desc)
            {
                order = Order.DESC;
            }
            o = new OrderSpecifier<String>(order, entityPath.getString(sortField));
        }
        List<T> contents = null;
        if (o == null)
        {
            contents = query.where(predicate).distinct().offset(offset).limit(entity.getCurPageSize()).fetch();
        }
        else
        {
            contents = query.where(predicate).orderBy(o).distinct().offset(offset).limit(entity.getCurPageSize())
                    .fetch();
        }
        // QUES：如果是为了列表显示，就只获取第一层的数据即可。
        if (dataLevel != null)
        {
            contents = replaceI18nFieldValueWithResource(dataClone(contents, dataLevel), languageId);
        }
        else
        {
            contents = replaceI18nFieldValueWithResource(dataClone(contents), languageId);
        }
        Page<T> res = new PageImpl<>(contents, generatePageable(entity, sortField, desc), total);
        return res;
    }

    @SuppressWarnings("unchecked")
    private <M extends GenericDbInfo> Predicate mergePredicate(Predicate predicate, PathBuilder<M> modelPath,
            Object object, Map<String, List<String>> i18nKeyFieldValues, boolean andCondition)
    {
        if (object == null)
        {
            return predicate;
        }
        M model = null;
        if (List.class.isAssignableFrom(object.getClass()))
        {
            List<M> models = (List<M>) object;
            if (models.size() == 0)
            {
                return predicate;
            }
            model = models.get(0);
        }
        else
        {
            model = (M) object;
        }
        ModelSpecification mSpec = modelSpecService.fetchModelSpecificationByClassName(model.getClass().getName(),
                CommonConstant.defaultSystemLanguage);
        Field[] fields = FieldValueUtil.getAllFields(model.getClass(), logger);
        String i18nKeyFieldName = null;
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(I18nKeyField.class))
            {
                i18nKeyFieldName = field.getName();
                if (i18nKeyFieldName != null && i18nKeyFieldName.length() > 0)
                {
                    List<String> keyFieldValues = i18nKeyFieldValues.get(model.getClass().getName());
                    if (keyFieldValues != null && keyFieldValues.size() > 0)
                    {
                        if (andCondition)
                        {
                            predicate = modelPath.getString(i18nKeyFieldName).in(keyFieldValues).and(predicate);
                        }
                        else
                        {
                            predicate = modelPath.getString(i18nKeyFieldName).in(keyFieldValues).or(predicate);
                        }
                    }
                }
            }
            if (!field.isAnnotationPresent(Column.class))
            {// 不是数据库中直接的字段，就不需要检索了
                continue;
            }
            if (field.isAnnotationPresent(I18nField.class))
            {// 如果是国际化字段，已经处理过，不需要再关心里面的值了。
                continue;
            }
            field.setAccessible(true);
            Object obj = FieldValueUtil.fetchFieldValue(model, field, logger);
            if (obj == null)
            {// 如果数据字段没有查询的数据，也不需要检索
                continue;
            }
            // QUES: 暂时支持：Boolean, Date, Integer, Long, Float, Double, String
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(Boolean.class))
            {
                if (andCondition)
                {
                    predicate = modelPath.getBoolean(field.getName()).eq((Boolean) obj).and(predicate);
                }
                else
                {
                    predicate = modelPath.getBoolean(field.getName()).eq((Boolean) obj).or(predicate);
                }
            }
            else if (fieldType.isAssignableFrom(String.class))
            {
                if (mSpec != null && StringUtils.isNotEmpty(mSpec.getTabField())
                        && mSpec.getTabField().equalsIgnoreCase(field.getName()))
                {
                    String[] allValues = (obj.toString().trim()).split(",");
                    if (andCondition)
                    {
                        predicate = modelPath.getString(field.getName()).in(allValues).and(predicate);
                    }
                    else
                    {
                        predicate = modelPath.getString(field.getName()).in(allValues).or(predicate);
                    }
                }
                else
                {
                    if (andCondition)
                    {
                        predicate = modelPath.getString(field.getName()).contains(obj.toString().trim()).and(predicate);
                    }
                    else
                    {
                        predicate = modelPath.getString(field.getName()).contains(obj.toString().trim()).or(predicate);
                    }
                }
            }
            else if (fieldType.isAssignableFrom(Integer.class))
            {
                if (andCondition)
                {
                    predicate = modelPath.getNumber(field.getName(), (Class<Integer>) fieldType).eq((Integer) obj)
                            .and(predicate);
                }
                else
                {
                    predicate = modelPath.getNumber(field.getName(), (Class<Integer>) fieldType).eq((Integer) obj)
                            .or(predicate);
                }
            }
            else if (fieldType.isAssignableFrom(Long.class))
            {
                if (andCondition)
                {
                    predicate = modelPath.getNumber(field.getName(), (Class<Long>) fieldType).eq((Long) obj)
                            .and(predicate);
                }
                else
                {
                    predicate = modelPath.getNumber(field.getName(), (Class<Long>) fieldType).eq((Long) obj)
                            .or(predicate);
                }
            }
            else if (fieldType.isAssignableFrom(Float.class))
            {
                if (andCondition)
                {
                    predicate = modelPath.getNumber(field.getName(), (Class<Float>) fieldType).eq((Float) obj)
                            .and(predicate);
                }
                else
                {
                    predicate = modelPath.getNumber(field.getName(), (Class<Float>) fieldType).eq((Float) obj)
                            .or(predicate);
                }
            }
            else if (fieldType.isAssignableFrom(Double.class))
            {
                if (andCondition)
                {
                    predicate = modelPath.getNumber(field.getName(), (Class<Double>) fieldType).eq((Double) obj)
                            .and(predicate);
                }
                else
                {
                    predicate = modelPath.getNumber(field.getName(), (Class<Double>) fieldType).eq((Double) obj)
                            .or(predicate);
                }
            }
            else if (fieldType.isAssignableFrom(Date.class))
            {
                if (andCondition)
                {
                    predicate = modelPath.getDate(field.getName(), (Class<Date>) fieldType).eq((Date) obj)
                            .and(predicate);
                }
                else
                {
                    predicate = modelPath.getDate(field.getName(), (Class<Date>) fieldType).eq((Date) obj)
                            .or(predicate);
                }
            }
        }
        return predicate;
    }

    private Pageable generatePageable(T entity, String sortField, boolean desc)
    {
        Direction d = Direction.ASC;
        if (desc)
        {
            d = Direction.DESC;
        }
        String s = "id";
        if (sortField != null && sortField.length() > 0)
        {
            s = sortField;
        }
        Sort sort = new Sort(d, s);
        Pageable pageable = PageRequest.of(entity.getPageIndex() - 1, entity.getCurPageSize(), sort);
        return pageable;
    }

    // 目前从数据库获取的模型的属性说明并不完善，只读了模型的基本数据类型的描述，
    // 如果属性是也是一个模型，则还没有从数据库读进来。（目前是读取当前模型后，模型内属性也是模型的属性说明数据）
    // 处理某持久化模型的属性说明
    // 1. 从数据库读取模型对应的FieldSpecification
    // 2. 按照注解读取模型的FieldSpecification
    // 3. 根据情况是采用数据库的值还是初始化的值（replaceValue）
    @Override
    public List<FieldSpecification> getFieldMetaData(Class<?> clazz, UserAccount userAcc, String languageId)
    {
        // 对有Entity注解 或者 继承GenericDbInfo 的model
        if (!clazz.isAnnotationPresent(Entity.class) && !GenericDbInfo.class.isAssignableFrom(clazz))
        {
            logger.info(clazz.getName() + " is not a persistent model or not extends GenericDbInfo.");
            return null;
        }
        Field[] fs = FieldValueUtil.getAllFields(clazz, logger);
        if (fs == null || fs.length == 0)
        {
            logger.info(clazz.getName() + " does not have any field.");
            return null;
        }
        Map<String, FieldSpecification> fieldSpecInDbs = loadFieldSpecificationFromDB(clazz, languageId);
        // 这里加载了通过注解获取到的最新FieldSpecification
        Map<String, FieldSpecification> initialFieldSpecs = loadFieldSpecificationByAnnotation(clazz, userAcc,
                languageId);
        List<FieldSpecification> fSpecs = mergeDbFieldSpecWithAnnotationFieldSpec(clazz, userAcc.getId(), languageId,
                fieldSpecInDbs, initialFieldSpecs);
        return fSpecs;
    }

    private List<FieldSpecification> mergeDbFieldSpecWithAnnotationFieldSpec(Class<?> modelClazz, Integer operId,
            String languageId, Map<String, FieldSpecification> fieldSpecInDbs,
            Map<String, FieldSpecification> initialFieldSpecs)
    {
        List<FieldSpecification> fSpecs = new ArrayList<FieldSpecification>();
        Field[] fs = FieldValueUtil.getAllFields(modelClazz, logger);
        for (Field f : fs)
        {
            if (!f.isAnnotationPresent(FieldMetaData.class))
            {
                continue;
            }
            String fieldFullName = modelClazz.getName() + "." + f.getName();
            FieldSpecification initialFieldSpec = initialFieldSpecs.get(fieldFullName);
            if (initialFieldSpec == null)
            {
                continue;
            }
            FieldSpecification fieldSpecInDb = fieldSpecInDbs.get(fieldFullName);
            if (fieldSpecInDb == null)
            {
                fieldSpecInDb = fieldSpecService.create(initialFieldSpec, operId, languageId);
            }
            else
            {
                if (fieldSpecInDb.getComponentMetaDatas() != null)
                {// 如果属性是类类型， 需要再继续处理类型说明
                    Map<String, FieldSpecification> subFieldSpecInDbs = fromList(fieldSpecInDb.getComponentMetaDatas());
                    Map<String, FieldSpecification> subInitialFieldSpecs = fromList(
                            initialFieldSpec.getComponentMetaDatas());
                    Class<?> fieldType = FieldValueUtil.getTypeOfField(f, logger);
                    List<FieldSpecification> subfSpecs = mergeDbFieldSpecWithAnnotationFieldSpec(fieldType, operId,
                            languageId, subFieldSpecInDbs, subInitialFieldSpecs);
                    if (subfSpecs.size() > 0)
                    {
                        fieldSpecInDb.setComponentMetaDatas(subfSpecs);
                    }
                }
                replaceValue(fieldSpecInDb, initialFieldSpec);
                initialFieldSpec.setId(fieldSpecInDb.getId());
                initialFieldSpec = fieldSpecService.update(fieldSpecInDb.getId(), initialFieldSpec, operId, languageId);
            }
            if (initialFieldSpec != null)
            {
                fSpecs.add(fieldSpecInDb);
            }
            else
            {
                logger.error("saveFieldSpecification error.");
            }
        }
        return fSpecs;
    }

    // msg：这里返回到前端使用的是fieldSpecInDb
    // ques：这里返回到前端使用的是fieldSpecInDb，label是国际化字段，直接取的是数据库数据返回的前端
    private void replaceValue(FieldSpecification fieldSpecInDb, FieldSpecification initialFieldSpec)
    {
        // 确定是使用数据库的值还是初始化配置的值
        // 会把initialFieldSpec的值持久化
        // fieldSpecInDb的值按照要求更新为initialFieldSpec的值之后，提交给页面使用
        Field[] fs = FieldValueUtil.getAllFields(FieldSpecification.class, logger);
        for (Field f : fs)
        {
            if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
            {// 忽略静态变量或者final的变量
                continue;
            }
            f.setAccessible(true);
            FieldMetaData fm = f.getAnnotation(FieldMetaData.class);
            boolean useDbValue = true;
            // 下面的情况不使用数据库的值
            // 1. 自动生成的值，是不允许被修改的
            if (fm != null && fm.autogenerated())
            {
                // 如果是自动生成的属性，使用FieldMetaData描述的初始化值
                useDbValue = false;
            }
            // 2. 不需要持久化的值，本来就不保存在数据库，也就不可能使用数据库的值。
            Transient notDbCol = f.getAnnotation(Transient.class);
            if (notDbCol != null)
            {
                useDbValue = false;
            }
            if (useDbValue)
            {
                // ques 这里返回到前端使用的是fieldSpecInDb，label是国际化字段，直接取的是数据库数据返回的前端，
                // 而这里是将实体字段的数据库值更新到初始化值，所以最新的initialFieldSpec里面的值等于不能再二次通过注解更新
                // 最终是要使用initialFieldSpec里面的值，
                // 所以，只要是需要使用数据库的情况，都要把数据库读取的信息设置到初始值
                try
                {
                    f.set(initialFieldSpec, f.get(fieldSpecInDb));
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
            else
            {
                try
                {
                    if (!f.isAnnotationPresent(Id.class))
                    {
                        if (!f.getName().equals("componentMetaDatas"))
                        {
                            f.set(fieldSpecInDb, f.get(initialFieldSpec));
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private Map<String, FieldSpecification> loadFieldSpecificationFromDB(Class<?> clazz, String languageId)
    {
        // 这个函数没有被嵌套，每次只是读取了一个业务类，以及其业务类属性的元数据
        List<FieldSpecification> fieldSpecInDbs = fetchFieldSpecsFromDb(clazz, languageId);
        return fromList(fieldSpecInDbs);
    }

    private List<FieldSpecification> fetchFieldSpecsFromDb(Class<?> clazz, String languageId)
    {
        List<FieldSpecification> fieldSpecInDbs = fieldSpecService.fetchByModelName(clazz.getName(), languageId);
        if (fieldSpecInDbs != null)
        {
            for (FieldSpecification fieldSpecInDb : fieldSpecInDbs)
            {
                Class<?> fieldType = FieldValueUtil.getTypeOfField(clazz, fieldSpecInDb.getName(), logger);
                if (fieldType.isAnnotationPresent(Embeddable.class)
                        || (fieldSpecInDb.getEnumerationName() == null && fieldType.isAnnotationPresent(Entity.class)))
                {// 把模型所有类类型属性的类属性说明都读出来。
                    List<FieldSpecification> subFieldSpecInDbs = fetchFieldSpecsFromDb(fieldType, languageId);
                    fieldSpecInDb.setComponentMetaDatas(subFieldSpecInDbs);
                }
            }
        }
        return fieldSpecInDbs;
    }

    private Map<String, FieldSpecification> fromList(List<FieldSpecification> fieldSpecs)
    {
        Map<String, FieldSpecification> fSpecs = new HashMap<String, FieldSpecification>();
        if (fieldSpecs == null || fieldSpecs.size() == 0)
        {
            return fSpecs;
        }
        for (FieldSpecification fieldSpec : fieldSpecs)
        {
            fSpecs.put(fieldSpec.getFieldFullName(), fieldSpec);
        }
        return fSpecs;
    }

    private Map<String, FieldSpecification> loadFieldSpecificationByAnnotation(Class<?> clazz, UserAccount userAcc,
            String languageId)
    {
        List<String> generatedClazzNames = new ArrayList<String>();
        List<FieldSpecification> fieldSpecs = getFieldMetaDataByAnnotation(clazz, generatedClazzNames, userAcc,
                languageId);
        return fromList(fieldSpecs);
    }

    // 子类使用这个方法来给前端返回验证业务模型的结果
    // 默认只给前端返回验证错误(弹窗)
    // 可根据各个业务模型不同,进行重写该方法
    @Override
    public String validateEntity(T entity, Integer operId, String languageId)
    {
        return getFilterMsg(entity);
    }

    // 这里使用public是为了在工作流里面也进行校验(只校验,不持久化错误信息),不通过时updatable
    // 这里是否合理呢?
    @Override
    public <M extends GenericDbInfo> String getFilterMsg(T entity)
    {

        if (entity != null)
        {
            List<ModelFilterStrategy> mfs = modelFilterStrategyService.findByClassFullName(entity.getClass().getName(),
                    CommonConstant.defaultSystemLanguage);
            if (mfs == null || mfs.size() == 0)
            {
                return null;
            }
            // 获取该model下设置的所有过滤条件
            List<ModelFilterStrategyItem> itemList = new ArrayList<>();
            for (ModelFilterStrategy m : mfs)
            {
                List<ModelFilterStrategyItem> items = m.getFilterItems();
                if (items != null && items.size() > 0)
                {
                    itemList.addAll(items);
                }
            }
            if (itemList != null && itemList.size() > 0)
            {
                Map<String, List<ModelFilterStrategyItem>> itemMap = itemList.stream().collect(Collectors
                        .groupingBy(ModelFilterStrategyItem::getFieldName, TreeMap::new, Collectors.toList()));

                try
                {
                    // 当前先做只支持本类字段和一层子类字段的校验,后面优化为遍历子类自循环校验
                    Field[] fields = entity.getClass().getDeclaredFields();
                    for (Field f : fields)
                    {
                        f.setAccessible(true);
                        Object v = f.get(entity);
                        Class<?> genericClazz = null;
                        if (f.getType().isAssignableFrom(List.class))
                        {
                            genericClazz = (genericClazz == null ? FieldValueUtil.getTypeOfField(f, logger)
                                    : genericClazz);
                            if (genericClazz == null || !GenericDbInfo.class.isAssignableFrom(genericClazz))
                            {
                                // 这个类不是持久化基类的子类，直接使用值就好了，这个情况应该不会出现的
                                continue;
                            }
                            // QUES: 不知道加得对不对
                            @SuppressWarnings("unchecked")
                            List<M> ids = (List<M>) v;
                            if (ids != null && ids.size() > 0)
                            {
                                for (M m : ids)
                                {
                                    String error = validateItemField(m, f, itemMap);
                                    if (StringUtils.isNotBlank(error))
                                    {
                                        return error;
                                    }
                                }
                            }
                        }
                        else if (f.getType().isAnnotationPresent(Entity.class))
                        {
                            genericClazz = FieldValueUtil.getTypeOfField(f, logger);
                            if (genericClazz == null || !GenericDbInfo.class.isAssignableFrom(genericClazz))
                            {
                                // 这个类不是持久化基类的子类，那就不用处理了
                                continue;
                            }
                            // QUES: 不知道加得对不对
                            @SuppressWarnings("unchecked")
                            M m = (M) v;
                            String error = validateItemField(m, f, itemMap);
                            if (StringUtils.isNotBlank(error))
                            {
                                return error;
                            }
                        }
                        else
                        {
                            List<ModelFilterStrategyItem> fitems = itemMap.get(f.getName());
                            if (fitems != null && fitems.size() > 0)
                            {
                                fitems.stream().sorted(Comparator.comparing(ModelFilterStrategyItem::getRank,
                                        Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());

                                try
                                {
                                    for (ModelFilterStrategyItem msfi : fitems)
                                    {
                                        String error = switchFilterStrategy(v, msfi);
                                        if (StringUtils.isNotBlank(error))
                                        {
                                            return error;
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    logger.error(e.getMessage(), e);
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private <M extends GenericDbInfo> String validateItemField(M m, Field f,
            Map<String, List<ModelFilterStrategyItem>> itemMap)
    {
        if (m != null && m.getClass() != null)
        {
            Field[] fieldItem = m.getClass().getDeclaredFields();
            if (fieldItem != null && fieldItem.length > 0)
            {
                for (Field fi : fieldItem)
                {
                    List<ModelFilterStrategyItem> fitems = itemMap.get(f.getName() + "." + fi.getName());
                    if (fitems != null && fitems.size() > 0)
                    {
                        fitems.stream().sorted(Comparator.comparing(ModelFilterStrategyItem::getRank,
                                Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
                        try
                        {
                            fi.setAccessible(true);
                            Object vi = fi.get(m);
                            for (ModelFilterStrategyItem msfi : fitems)
                            {
                                String error = switchFilterStrategy(vi, msfi);
                                if (StringUtils.isNotBlank(error))
                                {
                                    return error;
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return null;
    }

    private String switchFilterStrategy(Object object, ModelFilterStrategyItem model)
    {
        String message = null;
        String operator = model.getFilterOperator();
        String filterValue = model.getFilterValue();
        if (filterValue == null)
        {
            return message;
        }
        if (operator == null)
        {
            return message;
        }
        FilterOperator operatorType = FieldValueUtil.lookup(FilterOperator.class, operator, logger);
        if (operatorType != null)
        {
            try
            {
                List<String> stringList = new ArrayList<>();
                Boolean pass = false;
                switch (operatorType)
                {
                case EQ:
                    if (object.equals(filterValue))
                    {
                        pass = true;
                    }
                    break;
                case GT:
                    if ((Integer) object > Integer.valueOf(filterValue))
                    {
                        pass = true;
                    }
                    break;
                case GTE:
                    if ((Integer) object >= Integer.valueOf(filterValue))
                    {
                        pass = true;
                    }
                    break;
                case LT:
                    if ((Integer) object < Integer.valueOf(filterValue))
                    {
                        pass = true;
                    }
                    break;
                case LTE:
                    if ((Integer) object <= Integer.valueOf(filterValue))
                    {
                        pass = true;
                    }
                    break;
                case IN:
                    stringList = Arrays.asList(filterValue.split(",|，|\\s+|\n|\r"));
                    for (String s : stringList)
                    {
                        if (s.trim().equals((String) object))
                        {
                            pass = true;
                        }
                    }
                    break;
                case NOTIN:
                    Boolean allnotin = true;
                    stringList = Arrays.asList(filterValue.split(",|，|\\s+|\n|\r"));
                    for (String s : stringList)
                    {
                        if (s.trim().equals((String) object))
                        {
                            allnotin = false;
                        }
                    }
                    if (allnotin)
                    {
                        pass = true;
                    }
                    break;
                case ISNOTNULL:
                    if (StringUtils.isNotBlank((String) object))
                    {
                        pass = true;
                    }
                    break;
                case ISNULL:
                    if (StringUtils.isBlank((String) object))
                    {
                        pass = true;
                    }
                    break;
                case REGEXP:
                    Pattern pattern = Pattern.compile(filterValue);
                    if (pattern.matcher((String) object).matches())
                    {
                        pass = true;
                    }
                    break;
                // TODO: 没有对NE的处理过程
                case NE:
                    break;
                }
                if (!pass)
                {
                    message = model.getMessage();
                }
            }
            catch (Exception e)
            {
                logger.error("switch Filter Strategy  error.");
                e.printStackTrace();
            }
        }
        return message;
    }

    @Override
    public List<T> dealWithEntitys(List<T> entities)
    {
        return entities;
    }

    @Override
    public List<T> listByOwner(UserAccount owner, String languageId)
    {
        return null;
    }
}
