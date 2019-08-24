package com.polarj.common.utility.csv;

import com.polarj.common.utility.FieldValueUtil;
import org.slf4j.Logger;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class CsvFileUtils
{
    public static <T> String convertToCsvStringRecord(T obj, Class<T> clazz, Logger logger)
    {
        if (obj == null || clazz == null)
        {
            if (logger != null)
            {
                logger.warn("Does not have enough parameters.");
            }
            return null;
        }
        Field[] csvFields = hasCsvField(clazz, logger);
        if (csvFields == null || csvFields.length == 0)
        {
            if (logger != null)
            {
                logger.warn("{} does not have field for generating csv record.", clazz.getSimpleName());
            }
            return null;
        }
        String[] csvValues = new String[csvFields.length];
        for (int i = 0; i < csvFields.length; i++)
        {
            csvValues[i] = getFieldStringValue(obj, csvFields[i], logger);
        }
        CsvWriter cw = new CsvWriter(new StringWriter(), ',');
        String res = null;
        try
        {
            cw.writeRecord(csvValues);
            res = cw.contentToString();
            cw.close();
        }
        catch (Exception e)
        {
            return "";
        }
        return res == null ? "" : res.substring(0, res.length() - 1);
    }

    private static <T> String getFieldStringValue(T obj, Field f, Logger logger)
    {
        CsvField cf = f.getAnnotation(CsvField.class);
        if (cf == null)
        {
            return "";
        }
        Object fo = FieldValueUtil.fetchFieldValue(obj, f, logger);
        if (fo != null)
        {
            // 这里应该使用CsvField的fmt来格式化输出。
            return fo.toString();
        }
        return "";
    }

    private static Field[] hasCsvField(Class<?> clazz, Logger logger)
    {
        Field[] fields = FieldValueUtil.getAllFields(clazz, logger);
        if (fields == null || fields.length == 0)
        {
            return null;
        }
        List<Field> csvFields = new ArrayList<>();
        for (Field f : fields)
        {
            if (f.isAnnotationPresent(CsvField.class))
            {
                csvFields.add(f);
            }
        }
        csvFields.sort((o1, o2) -> {
            CsvField cf1 = o1.getAnnotation(CsvField.class);
            CsvField cf2 = o2.getAnnotation(CsvField.class);
            if (cf1 != null && cf2 != null)
            {
                return cf1.sn() - cf2.sn();
            }
            return 0;
        });
        return csvFields.toArray(new Field[1]);
    }
}
