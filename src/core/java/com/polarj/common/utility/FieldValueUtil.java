package com.polarj.common.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.collections4.list.UnmodifiableList;
import org.slf4j.Logger;

import com.polarj.model.GenericDbInfo;

public class FieldValueUtil
{
    public static <M extends GenericDbInfo, N extends M> List<N> createSubEntities(Class<N> nClass,
            List<M> parentEntities, Logger logger)
    {
        if (parentEntities == null || parentEntities.size() == 0)
        {
            return null;
        }
        List<N> entities = new ArrayList<N>();
        for (M parentEntity : parentEntities)
        {
            N entity = createSubEntity(nClass, parentEntity, logger);
            if (entity != null)
            {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static <M extends GenericDbInfo, N extends M> N createSubEntity(Class<N> nClass, M parentEntity,
            Logger logger)
    {
        if (nClass == null || parentEntity == null)
        {
            return null;
        }
        N entity = null;
        try
        {
            entity = nClass.newInstance();
            Field[] fields = getAllFields(parentEntity.getClass(), logger);
            for (Field field : fields)
            {
                field.setAccessible(true);
                if (Modifier.isFinal(field.getModifiers()))
                {
                    continue;
                }
                if (!field.isAnnotationPresent(Transient.class))
                {
                    field.set(entity, field.get(parentEntity));
                }
            }
        }
        catch (Exception e)
        {
            if (logger != null)
            {
                logger.error(e.getMessage(), e);
            }
            else
            {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public static final Collection<?> createCollection(Class<?> fieldType)
    {
        if (List.class.isAssignableFrom(fieldType))
        {
            ArrayList<?> res = new ArrayList<Object>();
            return res;
        }
        return null;
    }

    // 如果是普通字段，返回字段类型
    // 如果是List<?>类型字段的范型类型，返回<?>中?的类型
    public static final Class<?> getTypeOfField(Field f, Logger logger)
    {
        Class<?> genericClazz = null;
        Type fc = f.getGenericType();
        if (fc == null)
        {
            return null;
        }
        if (fc instanceof Class)
        {
            genericClazz = (Class<?>) fc;
        }
        else if (fc instanceof ParameterizedType)
        {
            ParameterizedType pt = (ParameterizedType) fc;

            genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
        }

        return genericClazz;
    }

    // 找出类中属性名为fieldName的数据类型，如果是集合类型，返回集合的基本类型
    public static final Class<?> getTypeOfField(Class<?> clazz, String fieldName, Logger logger)
    {
        Field f = getTheField(clazz, fieldName, logger);
        if(f == null) {
        	logger.error("cannot find filed :" + fieldName+" in " + clazz.getSimpleName());
        }
        return getTypeOfField(f, logger);
    }
    
    public static <T, S> void setFieldValue(T obj, String fieldName, S fieldValue, Logger logger)
    {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass();
        Field f = getTheField(clazz, fieldName, logger);
        if (f == null)
        {
            return;
        }

        if (fieldValue == null)
        {
            return;
        }
        if (Modifier.isFinal(f.getModifiers()))
        {
            return;
        }
        f.setAccessible(true);
        try
        {
            f.set(obj, fieldValue);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    public static <T, S> void setFieldValue(T obj, String fieldName, List<S> fieldValue, Logger logger)
    {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass();
        Field f = getTheField(clazz, fieldName, logger);
        if (f == null)
        {
            return;
        }

        if (fieldValue == null)
        {
            return;
        }
        if (Modifier.isFinal(f.getModifiers()))
        {
            return;
        }
        f.setAccessible(true);
        try
        {
            f.set(obj, fieldValue);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    public static <T> void setFieldValue(T obj, String fieldName, String fieldValue, SimpleDateFormat sdf,
            Logger logger)
    {
        if (sdf == null)
        {
            sdf = new SimpleDateFormat();
        }
        Class<?> clazz = obj.getClass();
        Field f = getTheField(clazz, fieldName, logger);
        if (f == null)
        {
            return;
        }

        if (fieldValue == null)
        {
            return;
        }

        f.setAccessible(true);
        Class<?> fieldTypeClazz = f.getType();
        String fieldTypeName = fieldTypeClazz.getSimpleName();
        try
        {
            if ("Date".equalsIgnoreCase(fieldTypeName))
            {
                Date date = sdf.parse(fieldValue);
                f.set(obj, date);
            }
            else if ("String".equalsIgnoreCase(fieldTypeName))
            {
                f.set(obj, fieldValue);
            }
            else if ("boolean".equals(fieldTypeName))
            {
                if ("true".equals(fieldValue) || "1".equals(fieldValue))
                {
                    f.setBoolean(obj, true);
                }
                else
                {
                    f.setBoolean(obj, false);
                }
            }
            else if ("Boolean".equals(fieldTypeName))
            {
                if ("null".equals(fieldValue))
                {
                    f.set(obj, null);
                }
                else if ("true".equals(fieldValue) || "1".equals(fieldValue))
                {
                    f.set(obj, Boolean.TRUE);
                }
                else
                {
                    f.set(obj, Boolean.FALSE);
                }
            }
            else if ("int".equalsIgnoreCase(fieldTypeName))
            {
                f.setInt(obj, Integer.parseInt(fieldValue));
            }
            else if ("Integer".equalsIgnoreCase(fieldTypeName))
            {
                if (fieldValue == "")
                {
                    f.set(obj, null);
                }
                else
                {
                    f.set(obj, new Integer(fieldValue));
                }
            }
            else if ("long".equals(fieldTypeName))
            {
                f.setLong(obj, Long.parseLong(fieldValue));
            }
            else if ("Long".equals(fieldTypeName))
            {
                if (fieldValue == "")
                {
                    f.set(obj, null);
                }
                else
                {
                    f.set(obj, new Long(fieldValue));
                }
            }
            else if ("short".equals(fieldTypeName))
            {
                f.setShort(obj, Short.parseShort(fieldValue));
            }
            else if ("Short".equals(fieldTypeName))
            {
                f.set(obj, new Short(fieldValue));
            }
            else if ("byte".equals(fieldTypeName))
            {
                f.setShort(obj, Byte.parseByte(fieldValue));
            }
            else if ("Byte".equals(fieldTypeName))
            {
                f.set(obj, new Byte(fieldValue));
            }
            else if ("float".equals(fieldTypeName))
            {
                f.setFloat(obj, Float.parseFloat(fieldValue));
            }
            else if ("Float".equals(fieldTypeName))
            {
                f.set(obj, new Float(fieldValue));
            }
            else if ("double".equals(fieldTypeName))
            {
                f.setDouble(obj, Double.parseDouble(fieldValue));
            }
            else if ("Double".equals(fieldTypeName))
            {
                f.set(obj, new Double(fieldValue));
            }
            else if ("char".equalsIgnoreCase(fieldTypeName))
            {
                f.setChar(obj, Character.valueOf(fieldValue.charAt(0)));
            }
            else if ("Character".equalsIgnoreCase(fieldTypeName))
            {
                f.set(obj, new Character(fieldValue.charAt(0)));
            }
            else if ("BigDecimal".equalsIgnoreCase(fieldTypeName))
            {
                f.set(obj, new BigDecimal(fieldValue));
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <O, FO> List<FO> fetchListFieldValue(O entity, String fieldName, Logger logger)
    {
        Class<?> clazz = entity.getClass();
        Field f = getTheField(clazz, fieldName, logger);
        if (f == null)
        {
            logger.info(clazz.getSimpleName() + " does not have field: '" + fieldName + "'");
            return null;
        }
        List<FO> existings = null;
        List<FO> res = new ArrayList<FO>();
        try
        {
            f.setAccessible(true);
            existings = (List<FO>) f.get(entity);
            if(existings!=null)
            {
                for (FO e : existings)
                {
                    res.add(e);
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            res = null;
        }
        if(existings!=null && existings.getClass().isAssignableFrom(UnmodifiableList.class))
        {
            return res;
        }
        else
        {
            return existings;
        }
    }

    public static <O, FO> FO fetchFieldValue(O entity, String fieldName, Logger logger)
    {
        if (entity == null)
        {
            return null;
        }
        Class<?> clazz = entity.getClass();
        Field f = getTheField(clazz, fieldName, logger);
        if (f == null)
        {
            logger.info(clazz.getSimpleName() + " does not have field: '" + fieldName + "'");
            return null;
        }
        FO fieldValue = fetchFieldValue(entity, f, logger);
        return fieldValue;
    }

    @SuppressWarnings("unchecked")
    public static <O, FO> FO fetchFieldValue(O entity, Field field, Logger logger)
    {
        if (field == null)
        {
            return null;
        }
        FO fieldValue = null;
        try
        {
            field.setAccessible(true);
            fieldValue = (FO) field.get(entity);
        }
        catch (IllegalArgumentException e)
        {
            logger.error("fetchFieldValue IllegalArgumentException: " + e.getMessage(), e);
            fieldValue = null;
        }
        catch (IllegalAccessException e)
        {
            logger.error("fetchFieldValue IllegalAccessException: " + e.getMessage(), e);
            fieldValue = null;
        }
        catch (Exception e)
        {
            logger.error("fetchFieldValue other exception: " + e.getMessage(), e);
            fieldValue = null;
        }
        return fieldValue;
    }

    public static Field[] getAllFields(Class<?> clazz, Logger logger)
    {
        if(clazz==null)
        {
            return null;
        }
        List<Field> fields = new ArrayList<Field>();
        while (!clazz.getSimpleName().equalsIgnoreCase("Object"))
        {
            Field[] fs = clazz.getDeclaredFields();
            if (fs != null)
            {
                for (Field f : fs)
                {
                    fields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[1]);
    }

    public static Field getTheField(Class<?> clazz, String fieldName, Logger logger)
    {
        Field f = null;
        if (fieldName == null || fieldName.length() == 0)
        {
            return null;
        }
        if (clazz.getSimpleName().equalsIgnoreCase("Object"))
            return f;
        try
        {
            f = clazz.getDeclaredField(fieldName);
            if (f != null)
            {
                return f;
            }
        }
        catch (NoSuchFieldException e)
        {
            return getTheField(clazz.getSuperclass(), fieldName, logger);
        }
        catch (Exception e)
        {
            logger.error("getTheField error :" + e.getMessage(), e);
            f = null;
        }
        return f;
    }

    public static <E extends Enum<E>> E lookup(Class<E> enumClass, String id, Logger logger) {
        E result = null;
        try
        {
            result = Enum.valueOf(enumClass, id);
        }
        catch (Exception e)
        {
            logger.error("No value of {} with name {}.", enumClass.getSimpleName(), id);
        }

        return result;
    }}
