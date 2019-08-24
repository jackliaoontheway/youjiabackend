package com.polarj.common.utility.csv;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.polarj.common.utility.FieldValueUtil;
import com.polarj.common.utility.report.FieldNameAndHeaderMapping;
import com.polarj.common.utility.report.ReportFileReaderWriter;

public class CsvReportFileReaderWriter<T> extends ReportFileReaderWriter<T>
{
    private CsvReader csvReader = null;

    public CsvReportFileReaderWriter(String datePattern, String datetimePattern, FieldNameAndHeaderMapping fieldNameAndHeaderMapping)
    {
        super(datePattern, datetimePattern, fieldNameAndHeaderMapping);
    }

    @Override
    public List<T> readReportFile(Class<T> rowClass, String fileName) throws Exception
    {
        csvReader = new CsvReader(fileName);
        List<T> datas = readFileHasColName(rowClass);
        return datas;
    }

    private List<T> readFileHasColName(Class<T> rowClass)
    {
        if (csvReader == null)
        {
            return null;
        }
        String[] fieldsNameUsed = getUsedFiledsName(rowClass);
        if (fieldsNameUsed == null)
        {
            return null;
        }
        List<T> rowObjectList = new ArrayList<T>();
        T obj = null;
        try
        {
            while (csvReader.readRecord())
            {
                String[] fieldValues = csvReader.getValues();
                obj = generateObjectFromCsvData(rowClass, fieldsNameUsed, fieldValues);
                if (obj == null)
                {
                    return null;
                }
                rowObjectList.add(obj);
            }
            csvReader.close();
        }
        catch (Exception e)
        {
            logger.error("readFileHasColName exception :" + e.getMessage(), e);
            rowObjectList = null;
        }
        return rowObjectList;
    }

    private T generateObjectFromCsvData(Class<T> rowClass, String[] fieldsName, String[] fieldValues) throws IOException
    {
        int i = 0;
        String str;
        Field f;
        if (fieldValues == null)
        {
            return null;
        }
        T obj = null;
        try
        {
            obj = rowClass.newInstance();
            for (i = 0; i < fieldsName.length; i++)
            {
                f = FieldValueUtil.getTheField(obj.getClass(), fieldsName[i], logger);
                if (f == null)
                    continue;
                if (Modifier.isFinal(f.getModifiers()))
                {
                    continue;
                }
                f.setAccessible(true);
                if (i >= fieldValues.length || fieldValues[i] == null || fieldValues[i].length() == 0)
                {
                    continue;
                }
                str = f.getType().getSimpleName();
                if (str.equals("int") || str.equals("Integer"))
                {
                    f.set(obj, Integer.valueOf(fixNumericStr(fieldValues[i])));
                }
                else if (str.equals("byte") || str.equals("Byte"))
                {
                    f.set(obj, Byte.valueOf(fixNumericStr(fieldValues[i])));
                }
                else if (str.equals("short") || str.equals("Short"))
                {
                    f.set(obj, Short.valueOf(fixNumericStr(fieldValues[i])));
                }
                else if (str.equals("long") || str.equals("Long"))
                {
                    f.set(obj, Long.valueOf(fixNumericStr(fieldValues[i])));
                }
                else if (str.equals("float") || str.equals("Float"))
                {
                    f.set(obj, Float.valueOf(fixNumericStr(fieldValues[i])));
                }
                else if (str.equals("double") || str.equals("Double"))
                {
                    f.set(obj, Double.valueOf(fixNumericStr(fieldValues[i])));
                }
                else if (str.equals("char") || str.equals("Character"))
                {
                    f.set(obj, Character.valueOf(fieldValues[i].charAt(0)));
                }
                else if (str.equals("boolean") || str.equals("Boolean"))
                {
                    String s = fieldValues[i];
                    if ("true".equals(s) || "1".equals(s))
                    {
                        f.set(obj, Boolean.TRUE);
                    }
                    else
                    {
                        f.set(obj, Boolean.FALSE);
                    }

                }
                else if (str.equals("String"))
                {
                    f.set(obj, fieldValues[i]);
                }
                else if (str.equals("Date"))
                {
                    try
                    {
                        if (dateTimeFormat != null)
                        {
                            f.set(obj, dateTimeFormat.parse(convertCriterionDate(fieldValues[i])));
                        }
                    }
                    catch (Exception e)
                    {
                        if (str.length() > 10)
                        {
                            if (dateTimeFormat != null)
                            {
                                f.set(obj, dateTimeFormat.parse(fieldValues[i]));
                            }
                        }
                        else
                        {
                            if (dateFormat != null)
                            {
                                f.set(obj, dateFormat.parse(fieldValues[i]));
                            }
                        }
                    }
                }
                else if (str.equals("BigDecimal"))
                {
                    f.set(obj, new BigDecimal(fieldValues[i]));
                }
                else
                {
                    f.set(obj, fieldValues[i]);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("generateObjectFromCsvData error :" + e.getMessage(), e);
            obj = null;
        }
        return obj;
    }

    private String[] getUsedFiledsName(Class<T> rowClass)
    {
        String[] fieldsNameUsed = null;
        try
        {
            String[] colsNameInFile = null;
            Map<String, String> csvHeaderFieldNameMap = fieldNameAndHeaderMapping.getHeaderToFieldNameMap();

            Set<String> headersShouldHave = csvHeaderFieldNameMap.keySet();

            // finding the header line
            boolean findHeader = false;
            while (!findHeader)
            {
                csvReader.readRecord();
                colsNameInFile = csvReader.getValues();
                // set findHeader=true if we find at least 2 headers which are should have
                int j = 0;
                for (String header : colsNameInFile)
                {
                    if (headersShouldHave.contains(header))
                    {
                        j++;
                    }
                    if (j > 2)
                    {
                        findHeader = true;
                    }
                }
            }
            if (colsNameInFile == null)
                return null;
            fieldsNameUsed = new String[colsNameInFile.length];

            for (int i = 0; i < colsNameInFile.length; i++)
            {
                String fieldName = csvHeaderFieldNameMap.get(colsNameInFile[i]);
                if (fieldName != null && fieldName.length() > 0)
                {
                    fieldsNameUsed[i] = fieldName;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("getUsedFiledsName exception :" + e.getMessage(), e);
            fieldsNameUsed = null;
        }
        return fieldsNameUsed;
    }

    private String fixNumericStr(String str)
    {
        str = removeComma(str);
        str = removeMoneyPrefix(str);
        return str;
    }

    private String removeComma(String str)
    {
        String[] StrArray = str.split(",");
        StringBuilder sb = new StringBuilder();
        for (String s : StrArray)
        {
            sb.append(s);
        }
        return sb.toString();
    }

    private String removeMoneyPrefix(String str)
    {
        char ch = str.charAt(0);
        if (!Character.isDigit(ch) && ch != '+' && ch != '-')
        {
            return str.substring(1);
        }
        return str;
    }

    private String convertCriterionDate(String dateStr)
    {
        int index = dateStr.indexOf(" ") == -1 ? dateStr.length() : dateStr.indexOf(" ");
        String start = "";
        String dateStr1 = dateStr.substring(0, index);
        String separateStr1 = dateStr1.substring(4, 5);
        String[] dateArray1 = dateStr1.split(separateStr1);
        if (dateArray1.length != 3)
        {
            return null;
        }

        if (dateArray1[1].length() == 1)
        {
            dateArray1[1] = "0" + dateArray1[1];
        }
        if (dateArray1[2].length() == 1)
        {
            dateArray1[2] = "0" + dateArray1[2];
        }
        start = dateArray1[0] + "-" + dateArray1[1] + "-" + dateArray1[2];

        String end = "";
        String dateStr2 = dateStr.substring(index + 1);
        if (dateStr2 != null && dateStr2.length() > 0)
        {
            String[] dateArray2 = dateStr2.split(":");
            if (dateArray2.length < 2)
            {
                return null;
            }

            if (dateArray2[0].length() == 1)
            {
                dateArray2[0] = "0" + dateArray2[0];
            }

            if (dateArray2[1].length() == 1)
            {
                dateArray2[1] = "0" + dateArray2[1];
            }

            if (dateArray2.length == 3)
            {
                if (dateArray2[2].length() == 1)
                {
                    dateArray2[2] = "0" + dateArray2[2];
                }
                end = dateArray2[0] + ":" + dateArray2[1] + ":" + dateArray2[2];
            }
            else
            {
                end = dateArray2[0] + ":" + dateArray2[1] + ":00";
            }
        }
        return start + " " + end;
    }

    @Override
    public void writeReportFile(List<T> content, String fileName)
    {

    }

}
