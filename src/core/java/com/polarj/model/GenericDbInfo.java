package com.polarj.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarj.common.CommonConstant;
import com.polarj.common.utility.FieldValueUtil;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.InJsonField;
import com.polarj.model.component.SearchScope;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
public @ToString @EqualsAndHashCode abstract class GenericDbInfo implements Cloneable, Serializable
{

    private static final long serialVersionUID = -2801114357875291333L;

    @Transient
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transient
    private @Getter @Setter Integer pageIndex = 1;

    @Transient
    private @Getter @Setter Integer curPageSize = 10;

    @Transient
    private @Getter List<I18nResource> i18nResources;

    @Transient
    private @Getter @Setter List<SearchScope> scopes;

    public void addI18nResource(I18nResource i18nResource)
    {
        addElementIntoList(i18nResources, "i18nResources", i18nResource);
    }

    @SuppressWarnings("unchecked")
    public <T extends GenericDbInfo> T deepClone(int depth, Logger logger)
    {
        T res = null;
        if (depth > 0)
        {
            res = (T) clone();
            Field[] fields = FieldValueUtil.getAllFields(res.getClass(), logger);
            for (Field field : fields)
            {
                Object o = null;
                try
                {
                    if (Modifier.isFinal(field.getModifiers()))
                    {
                        continue;
                    }
                    field.setAccessible(true);
                    o = field.get(res);
                    if (o == null)
                    {
                        continue;
                    }
                    if (o instanceof GenericDbInfo)
                    {
                        GenericDbInfo go = (GenericDbInfo) o;
                        GenericDbInfo no = go.deepClone(depth - 1, logger);
                        if (no != null)
                        {
                            field.set(res, no);
                        }
                        else
                        {
                            field.set(res, null);
                        }
                    }
                    if (o instanceof Collection<?>)
                    {
                        Collection<?> cos = (Collection<?>) o;
                        if (cos.size() == 0)
                        {
                            field.set(res, null);
                            continue;
                        }
                        Class<?> genericType = FieldValueUtil.getTypeOfField(field, logger);
                        Set<Integer> handledModels = new HashSet<Integer>();
                        if (GenericDbInfo.class.isAssignableFrom(genericType))
                        {
                            Collection<GenericDbInfo> ncos = (Collection<GenericDbInfo>) FieldValueUtil
                                    .createCollection(field.getType());
                            for (Object go : cos)
                            {
                                GenericDbInfo dbO = (GenericDbInfo) go;
                                if (handledModels.contains(dbO.getId()))
                                {
                                    continue;
                                }
                                handledModels.add(dbO.getId());
                                GenericDbInfo ngo = dbO.deepClone(depth - 1, logger);
                                if (ngo != null)
                                {
                                    ncos.add(ngo);
                                }
                            }
                            field.set(res, ncos);
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return res;
    }

    @Override
    protected Object clone()
    {
        return cloneImpl();
    }

    @SuppressWarnings("unchecked")
    private <T extends GenericDbInfo> T cloneImpl()
    {
        T res = null;
        try
        {
            res = (T) super.clone();
        }
        catch (Exception e)
        {
            res = null;
        }
        return res;
    }

    @FieldMetaData(autogenerated = true, label = "ID", position = 1, hide = true,
            dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private @Getter @Setter Integer id;

    @Column(nullable = false)
    @JsonIgnore
    private @Getter @Setter Date createdDate;

    @Column(nullable = false)
    @JsonIgnore
    private @Getter @Setter Integer createdBy;

    // 在添加有级联保存操作的模型时，顶层模型的创建者设置为操作者的ID
    // 级联模型的创建者暂时设置为系统操作员。QUES？
    @PrePersist
    void preInsertData()
    {
        if (createdBy == null)
        {
            createdBy = CommonConstant.systemUserAccountId;
        }
        createdDate = new Date();
    }

    protected <T extends GenericDbInfo> void addElementIntoList(List<T> elms, String fieldName, T elm)
    {
        if (elm == null)
        {
            logger.error("No elm information.");
            return;
        }
        if (elms == null)
        {
            elms = new ArrayList<T>();
            FieldValueUtil.setFieldValue(this, fieldName, elms, logger);
        }
        if (elm.getId() == null && !elms.contains(elm))
        {// elm is a brand new element, so add it.
            elms.add(elm);
            return;
        }
        if (elm.getId() != null)
        {// elm could be an existing element, check it and update it.
            for (GenericDbInfo gElm : elms)
            {
                if (gElm.getId() == null)
                {
                    continue;
                }
                if (gElm.getId().intValue() == elm.getId().intValue())
                {
                    elms.remove(gElm);
                    elms.add(elm);
                    return;
                }
            }
            elms.add(elm);
        }
    }

    // QUES
    // 目前使用注解FieldMetaData的属性来进行校验，
    // 后面应该使用保存的FieldSpecification来进行校验
    // 这样校验的数据可以在运行态进行修改。
    public boolean valid()
    {
        boolean res = true;
        Field[] fields = FieldUtils.getFieldsWithAnnotation(getClass(), FieldMetaData.class);
        try
        {
            for (Field field : fields)
            {
                field.setAccessible(true);
                FieldMetaData fmd = field.getAnnotation(FieldMetaData.class);
                if (fmd.required())
                {
                    Object o = field.get(this);
                    if (o == null)
                    {
                        return false;
                    }
                    if (o instanceof String)
                    {
                        if (((String) o).isEmpty())
                        {
                            return false;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            res = false;
        }
        return res;
    }

    // 在新增业务模型数据时，对于没有设置初始化值同时有缺省值配置的属性，
    // 利用该方法把缺省值配置好。
    // 同时业务模型可以使用此方法自行在新增业务模型数据前，做一些自定义的数据初始化工作
    public void initDefaultValueBeforeCreation()
    {

    }

    @Transient
    private String entityName = null;

    public String getEntityName()
    {
        if (entityName == null)
        {
            entityName = getClass().getSimpleName();
        }
        return entityName;
    }

    // 前端根据这个显示列表中的一行数据为特殊颜色，子类可以重写set方法来确定前端怎么显示
    @Transient
    private @Getter @Setter String rowFillColor;

    @Transient
    private @Getter @Setter String rowTextColor;

    // 业务模型可以制定若干需要快速查找的属性，这个非持久化属性保存那些字段的可能值
    // 指定的属性可以是组成该业务模型的其他业务模型的某属性的值，用“.”描述级别关系
    @Transient
    private @Getter @Setter String smartSearchText;

    @Transient
    @JsonIgnore
    private ObjectMapper objectMapper = new ObjectMapper();

    // 这个字段保存实现HasFieldInJson接口的业务模型中被@JsonView(InJsonField.class)注解的所有属性的值
    // 这个属性的值并不传送到前端
    @Column(length = 4095)
    @JsonIgnore
    private String jsonFields;

    public String getJsonFields()
    {
        try
        {
            objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
            jsonFields = objectMapper.writerWithView(InJsonField.class).writeValueAsString(this);
        }
        catch (Exception e)
        {
            jsonFields = "";
            logger.error(e.getMessage(), e);
        }
        return jsonFields;
    }

    public <T extends HasFieldInJson> void setJsonFields(String jsonFields)
    {
        if (jsonFields == null || jsonFields.length() == 0)
        {
            return;
        }
        try
        {
            objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
            T r = objectMapper.readerWithView(InJsonField.class).forType(this.getClass()).readValue(jsonFields);

            Field[] fields = FieldValueUtil.getAllFields(this.getClass(), logger);
            for (Field f : fields)
            {
                if (Modifier.isFinal(f.getModifiers()))
                {
                    continue;
                }
                f.setAccessible(true);
                if (f.get(r) != null)
                {
                    f.set(this, f.get(r));
                }
            }
            getJsonFields();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    public <T extends HasFieldInJson, D extends T> D castTo(Class<D> clazz)
    {
        Field[] fields = FieldValueUtil.getAllFields(this.getClass(), logger);
        String jsonFields = null;
        D dest = null;
        try
        {
            dest = clazz.newInstance();
            for (Field f : fields)
            {
                if (Modifier.isFinal(f.getModifiers()))
                {
                    continue;
                }
                f.setAccessible(true);
                if (f.getName().equalsIgnoreCase("jsonFields"))
                {
                    jsonFields = (String) f.get(this);
                }
                else
                {
                    if (f.get(this) != null)
                    {
                        f.set(dest, f.get(this));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        dest.setJsonFields(jsonFields);
        return dest;
    }
}
