package com.polarj.common.utility.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.polarj.common.utility.FieldValueUtil;
import com.polarj.common.utility.report.FieldNameAndHeaderMapping;
import com.polarj.common.utility.report.ReportFileReaderWriter;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.component.DecodeFromFormatter;

public class ExcelReportFileReaderWriter<T> extends ReportFileReaderWriter<T>
{
    public ExcelReportFileReaderWriter(FieldNameAndHeaderMapping fieldNameAndHeaderMapping)
    {
        super(fieldNameAndHeaderMapping);
    }

    private void createHeadRow(Sheet sheet)
    {
        Map<String, String> field2HeaderMap = fieldNameAndHeaderMapping.getFieldNameToHeaderMap();
        Row headRow = sheet.createRow(0);
        sheet.autoSizeColumn(0);
        int headColnum = 0;
        for (Map.Entry<String, String> map : field2HeaderMap.entrySet())
        {
            Cell cell = headRow.createCell(headColnum++);
            cell.setCellValue(map.getValue());
        }

    }

    private void writeCellData(Workbook workbook, Cell cell, Date cellValue)
    {
        CellStyle cellStyle = workbook.createCellStyle();
        String dateformatStr = dateTimeFormat.toPattern();// "yyyy-MM-dd HH:mm:ss";
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(dateformatStr));
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);
    }

    // 返回一共写了多少个单元
    private int writeEntityIntoCell(Workbook workbook, Row row, int cellPosition, Set<String> allFieldNames,
            String trueFieldName, Object filedValue)
    {
        int cellnumWrited = 0;
        for (String fieldNameInSet : allFieldNames)
        {
            if (fieldNameInSet.contains(trueFieldName))
            {
                String subFieldName = fieldNameInSet.substring(trueFieldName.length() + 1);
                Object subFieldValue = FieldValueUtil.fetchFieldValue(filedValue, subFieldName, logger);
                // 这里是一个假设，所有的类类型的属性都是普通数据。
                writeNormalDataIntoCell(workbook, row, cellnumWrited + cellPosition, subFieldValue);
                cellnumWrited = cellnumWrited + 1;
            }
        }
        return cellnumWrited;
    }

    private void writeNormalDataIntoCell(Workbook workbook, Row row, int cellPosition, Object filedValue)
    {
        Cell cell = row.createCell(cellPosition);
        if (filedValue == null)
        {
            return;
        }
        if (filedValue instanceof Date)
        {
            writeCellData(workbook, cell, (Date) filedValue);
        }
        else if (filedValue instanceof Boolean)
        {
            cell.setCellValue((Boolean) filedValue);
        }
        else if (filedValue instanceof String)
        {
            cell.setCellValue((String) filedValue);
        }
        else if (filedValue instanceof Short)
        {
            cell.setCellValue((Short) filedValue);
        }
        else if (filedValue instanceof Integer)
        {
            cell.setCellValue((Integer) filedValue);
        }
        else if (filedValue instanceof Long)
        {
            cell.setCellValue((Long) filedValue);
        }
        else if (filedValue instanceof Float)
        {
            cell.setCellValue((Float) filedValue);
        }
        else if (filedValue instanceof Double)
        {
            cell.setCellValue((Double) filedValue);
        }
        else if (filedValue instanceof DecodeFromFormatter)
        {
            // FIXME: 应该有一个格式字符串参数作为格式。
            cell.setCellValue((String) ((DecodeFromFormatter) filedValue).toString(""));
        }
        else
        {
            cell.setCellValue(filedValue.toString());
        }
    }

    private void writeEnumerationIntoCell(Row row, int cellPosition, String labelFieldName, Object filedValue)
    {
        Object subFieldValue = FieldValueUtil.fetchFieldValue(filedValue, labelFieldName, logger);
        if (subFieldValue != null)
        {
            Cell cell = row.createCell(cellPosition);
            cell.setCellValue(subFieldValue.toString());
        }
    }

    private void writeListOfEnumerationIntoCell(Row row, int cellPosition, String labelFieldName, Object filedValue)
    {
        boolean isList = (filedValue instanceof List);
        if (!isList)
        {
            logger.info("Expected List Data, but the data is " + filedValue.getClass().getSimpleName());
            return;
        }
        List<?> objs = (List<?>) filedValue;
        String value = "";
        for (Object obj : objs)
        {
            Object subFieldValue = FieldValueUtil.fetchFieldValue(obj, labelFieldName, logger);
            value = value + subFieldValue.toString() + ",";
        }
        Cell cell = row.createCell(cellPosition);
        cell.setCellValue(value);
    }

    // 返回一共写入多少列
    private int writeListOfEntityIntoCell(Workbook workbook, Sheet sheet, int rowPosition, int cellPosition,
            Set<String> allFieldNames, String trueFieldName, Object filedValue)
    {
        boolean isList = (filedValue instanceof List);
        if (!isList)
        {
            logger.info("Expected List Data, but the data is " + filedValue.getClass().getSimpleName());
            return 0;
        }
        List<?> objs = (List<?>) filedValue;
        int cellNumWrited = 0;
        for (Object obj : objs)
        {
            // 判断是否之前已经create过row了,如果已有row,(主表or前一个list创建的)
            // 取出已有数据在不删除已有数据前提下添加数据
            // 否则创建新的一行
            Row row = sheet.getRow(rowPosition);
            if(row == null)
            {
                row = sheet.createRow(rowPosition);
            }
            cellNumWrited = writeEntityIntoCell(workbook, row, cellPosition, allFieldNames, trueFieldName, obj);
            rowPosition++;
        }
//        for (Object obj : objs)
//        {
//            Row row = sheet.createRow(rowPosition);
//            cellNumWrited = writeEntityIntoCell(workbook, row, cellPosition, allFieldNames, trueFieldName, obj);
//            rowPosition++;
//        }
        return cellNumWrited;
    }

    /**
     * T filed dateformatStr chose the dateFormat
     */
    @Override
    final public void writeReportFile(List<T> content, String fileName) throws Exception
    {
        if (null == content || content.size() == 0)
        {
            logger.error("writeReportFile(List<T> content, String fileName) the content cann't be empty");
            return;
        }

        // get simpleFileName
        String fileName_suffix = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
        String sheetName =
                fileName_suffix.substring(0, fileName_suffix.lastIndexOf(".")).toLowerCase().replace("/", "-");
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        Map<String, String> field2HeaderMap = fieldNameAndHeaderMapping.getFieldNameToHeaderMap();

        Integer size = content.size();

        createHeadRow(sheet);
        int rownum = 0; // 记录excel的当前行
        for (int i = 0; i < size; i++)
        {
            int cellnum = 0; // 记录excel的当前列
            int maxRow = 0;
            rownum++;
            Row row = sheet.createRow(rownum);
            sheet.autoSizeColumn((short) (rownum));
            // 重点处理List和类类型的数据导出
            // 1. List数据
            // List的数据如果是非类类型，直接按照文本方式连接所有数据内容，
            // List的数据是类类型的，有两种情况：
            // 一个是一对多枚举类型或者类似枚举类型（多一端的类型是类类型，但是只关心其核心值）的。
            // 解决方案： field的值为“模型内的属性名称(作为值的类类型的属性名)”
            // 一种是一对多的父子类型的，多类型的所有属性都需要输出的
            // 解决方案： field的值为“模型内属性名.类类型的属性名”
            // 2. 类类型数据
            // 如果field的值是模型的属性名，直接用类类型的toString方法提供String的值
            // 如果field的值是“模型内属性名.类类型的属性名”，则直接使用类类型的属性值
            // 3. 只输出两层数据，不再继续嵌套
            T record = content.get(i);
            List<String> handledFields = new ArrayList<String>();
            for (String fieldName : field2HeaderMap.keySet())
            {
                if (fieldName.contains("(") || fieldName.contains("."))
                {
                    // List数据或者需要处理的类类型数据
                    if (fieldName.contains("("))
                    {// 属性是枚举或者类似枚举的类型
                        String truefieldName = fieldName.substring(0, fieldName.indexOf("("));
                        if (handledFields.contains(truefieldName))
                        {
                            continue;
                        }
                        handledFields.add(truefieldName);
                        String labelFieldName = fieldName.substring(fieldName.indexOf("(") + 1, fieldName.indexOf(")"));
                        Object filedValue = FieldValueUtil.fetchFieldValue(record, truefieldName, logger);
                        if (filedValue instanceof List<?>)
                        {
                            writeListOfEnumerationIntoCell(row, cellnum, labelFieldName, filedValue);
                        }
                        else
                        {
                            writeEnumerationIntoCell(row, cellnum, labelFieldName, filedValue);
                        }
                        cellnum = cellnum + 1;
                    }
                    else
                    {// 属性是类类型数据
                        String truefieldName = fieldName.substring(0, fieldName.indexOf("."));
                        if (handledFields.contains(truefieldName))
                        {
                            continue;
                        }
                        handledFields.add(truefieldName);
                        Object filedValue = FieldValueUtil.fetchFieldValue(record, truefieldName, logger);
                        int cellnumWrited = 0;
                        if (filedValue instanceof List<?>)
                        {
                            // 改动说明:
                            // 传入当前行数
                            // 如果list只有一个,将list的第一条提到一一行显示是没有问题的,因为每个list的主表信息都一样的
                            // ques:但是!!!!如果多个list呢?list>=1时,将第一条提到主表的那一行显示
                            // 如果多个list之间没有关联性,多个list并行显示好像有点问题,不符合逻辑,
                            // 现在针对他们业务,只有一个shipmentInfo,多个item是没有问题的,后面如果多包裹,这个就会有问题
                            // 需要跟向老师探讨一下.
                            cellnumWrited = writeListOfEntityIntoCell(workbook, sheet, rownum, cellnum,
                                    field2HeaderMap.keySet(), truefieldName, filedValue);
                            int thisRow = rownum + (((List<?>) filedValue).size() >0 ? ((List<?>) filedValue).size()-1 : 0);
                            // 这里做一步操作,为了取出有多个list时,最大list的条数,取其作为最大行数
                            if(thisRow > maxRow){
                                maxRow = thisRow;
                                rownum = maxRow; // 将319行的 rownum = maxRow 放到这里
                            }
                        }
                        else
                        {
                            cellnumWrited = writeEntityIntoCell(workbook, row, cellnum, field2HeaderMap.keySet(),
                                    truefieldName, filedValue);
                        }
                        cellnum = cellnum + cellnumWrited;
                    }
                }
                else
                {
                    // 常规数据，或者不需要处理的，直接使用toString方法得到值的类类型数据
                    Object filedValue = FieldValueUtil.fetchFieldValue(record, fieldName, logger);
                    writeNormalDataIntoCell(workbook, row, cellnum, filedValue);
                    cellnum = cellnum + 1;
                }
            }
            // rownum = maxRow;
        }
        FileOutputStream out = new FileOutputStream(new File(fileName));
        workbook.write(out);
        out.close();
        logger.info("Excel create successfully..");
        workbook.close();
    }

    @Override
    final public List<T> readReportFile(Class<T> rowClass, String fileName)
    {
        List<T> list = null;
        try
        {
            inputFileChartSet = guessFileEncoding(fileName);
            Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName));
            list = readReportFile(rowClass, workbook);
            workbook.close();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            list = null;
        }
        return list;
    }

    private String[] fetchFieldNames(Row row)
    {
        String[] fieldNames = null;
        Map<String, String> header2FieldMap = fieldNameAndHeaderMapping.getHeaderToFieldNameMap();
        if (row.getRowNum() == 1)
        {// it is the header
            int totalCol = row.getLastCellNum();
            String[] header = new String[totalCol];
            fieldNames = new String[header.length];
            for (int i = 0; i < totalCol; i++)
            {
                header[i] = row.getCell(i).getStringCellValue();
                fieldNames[i] = header2FieldMap.get(header[i]);
                // 当得到的属性名称为空时，直接使用标题栏（可能就是属性名）尝试一下是否能够设置好数据。
                fieldNames[i] = (fieldNames[i] == null ? header[i] : fieldNames[i]);
            }
        }
        return fieldNames;
    }

    // 判断一下是不是空行，数据列都是空或者null
    private boolean isEmpty(Row row)
    {
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext())
        {
            Cell c = cellIterator.next();
            if (c != null)
            {
                String v = cell2String(c);
                if (StringUtils.isNotEmpty(v))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private List<T> readReportFile(Class<T> rowClass, Workbook workbook)
            throws InstantiationException, IllegalAccessException
    {
        List<T> list = new ArrayList<T>();
        List<String> mapName2List = new ArrayList<String>();
        Map<String, String> name2HeadMap = fieldNameAndHeaderMapping.getFieldNameToHeaderMap();
        for (Map.Entry<String, String> map : name2HeadMap.entrySet())
        {
            mapName2List.add(map.getKey());
        }

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        String[] fieldNames = null;
        if (rowIterator.hasNext())
        {
            // 第一行是注释，忽略掉
            rowIterator.next();
        }
        if (rowIterator.hasNext())
        {
            // 第二行转换成业务模型的属性名数组
            fieldNames = fetchFieldNames(rowIterator.next());
        }
        if (fieldNames == null || fieldNames.length == 0)
        {
            return list;
        }
        // 找出所有的属性是业务模型的类型，分三类：独立管理的，级联管理对一关系的，级联管理对多关系的
        Map<String, Class<?>> managedSeperatelyModelClazzes = findAllManagedSeperatelyModelClass(rowClass, fieldNames);
        Map<String, Class<?>> anyToOneModelClazzes = findAllAnyToOneModelClass(rowClass, fieldNames);
        Map<String, Class<?>> anyToManyModelClazzes = findAllAnyToManyModelClass(rowClass, fieldNames);
        // 该对象保存最后一次生成的业务模型对象，
        // 如果当前行是业务模型的列类型属性数据，就要用到这个值来处理数据
        T lastCreatedEntity = null;
        // 上传数据的表头对应的属性名称按照如下方式分类
        // 1. 基本数据类型：直接使用属性名
        // 2. 需要级联操作的业务模型（如：国家和省市）：属性名.属性类的属性名
        // 3. 没有详情的业务模型数据（如：国家与货币，货币是单独管理且可以没有详情的业务模型数据，一些模型中的onwer也是类似的）：
        // 属性名（属性类的唯一性属性名）
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            if (row.getRowNum() < 2) // 前面两行是注释和表头
            {
                continue;
            }
            if (isEmpty(row))
            {
                continue;
            }
            T t = null;
            // 数据表的第一列要求是业务模型的一个不可以为空的简单类型属性，
            // 把第一列用于判断该行是否是一个完整的业务模型
            // 如果是，就用该行创建一个业务模型
            // 如果不是，说明该行数据是前面业务模型的列表类，列表元素是非独立管理的业务模型类数据（如：国家和省份）
            String firstColValue = fetchCellValue(rowClass, row.getCell(0), fieldNames[0]);
            if (firstColValue != null && firstColValue.length() != 0)
            {
                t = rowClass.newInstance();
                // FieldValueUtil.setFieldValue(t, fieldNames[0], firstColValue, null, logger);
                lastCreatedEntity = t;
            }
            if (t != null)
            {
                // 把所有基本类型的数据保存到创建的对象中
                saveExcelDataToNonmodelField(rowClass, t, fieldNames, row);
                // 把所有非独立管理的对一关系级联数据保存到创建的对象中（如：用户帐号与联系人，用户帐号与用户设置）
                for (String anyToOneFieldName : anyToOneModelClazzes.keySet())
                {
                    Class<?> anyToOneModelClazz = anyToOneModelClazzes.get(anyToOneFieldName);
                    Object obj = generateFieldModelObject(anyToOneModelClazz, anyToOneFieldName, fieldNames, row);
                    FieldValueUtil.setFieldValue(t, anyToOneFieldName, obj, logger);
                }
                // 把所有独立管理属性类的值保存到创建的对象中（如：用户账户和用户角色）
                for (String managedSeperatelyFieldName : managedSeperatelyModelClazzes.keySet())
                {
                    Class<?> managedSeperatelyClazz = managedSeperatelyModelClazzes.get(managedSeperatelyFieldName);
                    Object obj = generateFieldModelObject(managedSeperatelyClazz, managedSeperatelyFieldName,
                            fieldNames, row);
                    FieldValueUtil.setFieldValue(t, managedSeperatelyFieldName, obj, logger);
                }
                // 把所有非独立管理的对多关系级联数据保存到创建的对象中（如：国家和省份）
                for (String anyToManyFieldName : anyToManyModelClazzes.keySet())
                {
                    List<Object> values = FieldValueUtil.fetchListFieldValue(t, anyToManyFieldName, logger);
                    if (values == null)
                    {
                        values = new ArrayList<Object>();
                        FieldValueUtil.setFieldValue(t, anyToManyFieldName, values, logger);
                    }
                    Class<?> anyToManyModelClazz = anyToManyModelClazzes.get(anyToManyFieldName);
                    Object obj = generateFieldModelObject(anyToManyModelClazz, anyToManyFieldName, fieldNames, row);
                    values.add(obj);
                }
                list.add(t);
            }
            else
            {
                // 把所有非独立管理的对多关系级联数据保存到创建的对象中（如：国家和省份）
                for (String anyToManyFieldName : anyToManyModelClazzes.keySet())
                {
                    List<Object> values =
                            FieldValueUtil.fetchListFieldValue(lastCreatedEntity, anyToManyFieldName, logger);
                    if (values == null)
                    {
                        values = new ArrayList<Object>();
                        FieldValueUtil.setFieldValue(lastCreatedEntity, anyToManyFieldName, values, logger);
                    }
                    Class<?> anyToManyModelClazz = anyToManyModelClazzes.get(anyToManyFieldName);
                    Object obj = generateFieldModelObject(anyToManyModelClazz, anyToManyFieldName, fieldNames, row);
                    values.add(obj);
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private Object generateFieldModelObject(Class<?> modelClazz, String firstFieldName, String[] fieldNames, Row row)
            throws InstantiationException, IllegalAccessException
    {
        Object obj = null;
        for (int i = 1; i < fieldNames.length; i++)
        {
            if (!fieldNames[i].startsWith(firstFieldName))
            {
                // 如果不是上层业务模型的模型属性，继续找下一个数据
                continue;
            }
            String fieldNameOfFieldModel = fieldNames[i].substring(firstFieldName.length() + 1);
            boolean managedSeperatelyModel = false;
            if (fieldNameOfFieldModel.indexOf(")") != -1)
            {
                fieldNameOfFieldModel = fieldNameOfFieldModel.substring(0, fieldNameOfFieldModel.length() - 1);
                managedSeperatelyModel = true;
            }
            String cellValue = fetchCellValue(modelClazz, row.getCell(i), fieldNameOfFieldModel);
            boolean generateOneEntity = true;
            // 独立管理的业务属性类，还需要判断是对一的关系还是对多的关系
            // QUES：cellValue 如果有“,”分隔符，就确定这是一个对多关系。
            // 对一的关系，与非独立管理的业务属性类同样处理即可
            if (managedSeperatelyModel && cellValue.indexOf(",") != -1)
            {
                generateOneEntity = false;
            }
            if (generateOneEntity)
            {
                if (obj == null)
                {
                    obj = modelClazz.newInstance();
                }
                fetchAndSaveCellValue(modelClazz, obj, row.getCell(i), fieldNameOfFieldModel);
            }
            else
            {
                if (obj == null)
                {
                    obj = new ArrayList<Object>();
                    // FieldValueUtil.setFieldValue(obj, fieldNameOfFieldModel, cellValue, null, logger);
                    fetchAndSaveCellValue(modelClazz, obj, row.getCell(i), fieldNameOfFieldModel);
                }
                String[] elmUniqueValues = cellValue.split(",");
                for (String elmUniqueValue : elmUniqueValues)
                {
                    Object elmObj = modelClazz.newInstance();
                    FieldValueUtil.setFieldValue(elmObj, fieldNameOfFieldModel, elmUniqueValue, null, logger);
                    // fetchAndSaveCellValue(modelClazz, elmObj, row.getCell(i), fieldNameOfFieldModel);
                    ((List<Object>) obj).add(elmObj);
                }
            }
        }
        return obj;
    }

    private Map<String, Class<?>> findAllManagedSeperatelyModelClass(Class<T> rowClass, String[] fieldNames)
    {
        return findAllModelClass(rowClass, fieldNames, null, true);
    }

    private Map<String, Class<?>> findAllAnyToManyModelClass(Class<T> rowClass, String[] fieldNames)
    {
        List<Class<? extends Annotation>> a = new ArrayList<Class<? extends Annotation>>();
        a.add(ManyToMany.class);
        a.add(OneToMany.class);
        return findAllModelClass(rowClass, fieldNames, a, false);
    }

    private Map<String, Class<?>> findAllAnyToOneModelClass(Class<T> rowClass, String[] fieldNames)
    {
        List<Class<? extends Annotation>> a = new ArrayList<Class<? extends Annotation>>();
        a.add(ManyToOne.class);
        a.add(OneToOne.class);
        return findAllModelClass(rowClass, fieldNames, a, false);
    }

    private Map<String, Class<?>> findAllModelClass(Class<T> rowClass, String[] fieldNames,
            List<Class<? extends Annotation>> annotationClasses, boolean wantManagedSeperately)
    {
        Map<String, Class<?>> clazzes = new HashMap<String, Class<?>>();
        for (int i = 1; i < fieldNames.length; i++)
        {
            String fieldName = fieldNames[i];
            String firstFieldName = null;
            boolean isCascadeModel = (fieldName.indexOf(".") != -1);
            boolean isModel = (fieldName.indexOf("(") != -1);
            if (!isCascadeModel && !isModel)
            {
                continue;
            }
            if (isCascadeModel)
            {
                firstFieldName = fieldName.substring(0, fieldName.indexOf("."));
            }
            if (isModel)
            {
                firstFieldName = fieldName.substring(0, fieldName.indexOf("("));
            }
            Field firstField = FieldValueUtil.getTheField(rowClass, firstFieldName, logger);
            Class<?> fieldType = firstField.getType();
            if (!wantManagedSeperately)
            {
                boolean hasAnnotation = false;
                for (int j = 0; j < annotationClasses.size(); j++)
                {
                    hasAnnotation = (hasAnnotation || firstField.isAnnotationPresent(annotationClasses.get(j)));
                }
                if (!hasAnnotation)
                {
                    continue;
                }
            }
            FieldMetaData fm = firstField.getAnnotation(FieldMetaData.class);
            if (fm == null)
            {
                continue;
            }
            if ((wantManagedSeperately && fm.managementSeparately())
                    || (!wantManagedSeperately && !fm.managementSeparately()))
            {
                if (List.class.isAssignableFrom(fieldType))
                {
                    Class<?> fieldClass = FieldValueUtil.getTypeOfField(firstField, logger);
                    if (clazzes.get(firstFieldName) == null)
                    {
                        clazzes.put(firstFieldName, fieldClass);
                    }
                }
                else
                {
                    if (clazzes.get(firstFieldName) == null)
                    {
                        clazzes.put(firstFieldName, fieldType);
                    }
                }
            }
        }
        return clazzes;
    }

    private void saveExcelDataToNonmodelField(Class<T> rowClass, T t, String[] fieldNames, Row row)
    {
        for (int i = 0; i < fieldNames.length; i++)
        {
            String fieldName = fieldNames[i];
            boolean isCascadeModel = (fieldName.indexOf(".") != -1);
            boolean isModel = (fieldName.indexOf("(") != -1);
            if (!isCascadeModel && !isModel)
            {
                fetchAndSaveCellValue(rowClass, t, row.getCell(i), fieldName);
            }
        }
    }

    private <O> String fetchAndSaveCellValue(Class<O> rowClass, Object t, Cell c, String fieldName)
    {
        String res = "";
        if (c == null)
        {
            return res;
        }
        Field field = FieldValueUtil.getTheField(rowClass, fieldName, logger);
        if (field == null)
        {
            return res;
        }
        Class<?> fieldCz = field.getType();
        if (fieldCz.equals(java.lang.Short.class) || fieldCz.equals(short.class))
        {
            Short shortValue = cell2Short(c);
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, shortValue, logger);
            }
            res = (shortValue == null ? "" : shortValue.toString());
        }
        else if (fieldCz.equals(java.lang.Integer.class) || fieldCz.equals(int.class))
        {
            Integer intValue = cell2Int(c);
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, intValue, logger);
            }
            res = (intValue == null ? "" : intValue.toString());
        }
        else if (fieldCz.equals(java.lang.Long.class) || fieldCz.equals(long.class))
        {
            Long longValue = cell2Long(c);
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, longValue, logger);
            }
            res = (longValue == null ? "" : longValue.toString());
        }
        else if (fieldCz.equals(java.lang.Float.class) || fieldCz.equals(float.class))
        {
            Float floatValue = cell2Float(c);
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, floatValue, logger);
            }
            res = (floatValue == null ? "" : floatValue.toString());
        }
        else if (fieldCz.equals(java.lang.Double.class) || fieldCz.equals(double.class))
        {
            Double d = cell2Double(c);
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, d, logger);
            }
            res = (d == null ? "" : d.toString());
        }
        else if (fieldCz.equals(java.lang.String.class))
        {
            String str = cell2String(c);
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, str, logger);
            }
            res = str;
        }
        else if (fieldCz.equals(java.util.Date.class))
        {
            Date d = cell2Date(c);
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, d, logger);
            }
            res = (d == null ? "" : dateTimeFormat.format(d));
        }
        else if (fieldCz.equals(java.lang.Boolean.class) || fieldCz.equals(boolean.class))
        {
            Boolean b = cell2Boolean(c);
            res = (b == null ? "" : b.toString());
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, b, logger);
            }
        }
        else if (fieldCz.equals(java.math.BigDecimal.class))
        {
            BigDecimal big = cell2BigDecimal(c);
            res = (big == null ? "" : big.toString());
            if (t != null)
            {
                FieldValueUtil.setFieldValue(t, fieldName, big, logger);
            }
        }
        if (field.isAnnotationPresent(Embedded.class))
        {
            String str = cell2String(c);
            res = str;
            FieldMetaData fm = field.getAnnotation(FieldMetaData.class);
            if (StringUtils.isNotEmpty(str) && DecodeFromFormatter.class.isAssignableFrom(fieldCz))
            {
                Object o = null;
                try
                {
                    o = generateModelByFormatter(fieldCz, fm.formatter(), str);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                if (o != null && t != null)
                {
                    FieldValueUtil.setFieldValue(t, fieldName, o, logger);
                }
            }
        }
        return res;
    }

    private <O> O generateModelByFormatter(Class<O> comClass, String formatter, String value)
    {
        O o = null;
        try
        {
            o = comClass.newInstance();
            ((DecodeFromFormatter) o).setFieldValue(formatter, value);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            o = null;
        }
        return o;
    }

    private <O> String fetchCellValue(Class<O> rowClass, Cell c, String fieldName)
    {
        return fetchAndSaveCellValue(rowClass, null, c, fieldName);
    }

    private Short cell2Short(Cell cell)
    {
        Short s = null;

        switch (cell.getCellType())
        {
        case STRING:
            s = Short.parseShort(cell.getStringCellValue());
            break;
        case NUMERIC:
            s = (short) cell.getNumericCellValue();
            break;
        case BOOLEAN:
            break;
        case BLANK:
            break;
        default:
            break;
        }
        return s;
    }

    private Integer cell2Int(Cell cell)
    {
        switch (cell.getCellType())
        {
        case STRING:
            return Integer.parseInt(cell.getStringCellValue());
        case NUMERIC:
            return (int) cell.getNumericCellValue();
        case BOOLEAN:
            return null;
        case BLANK:
            return null;
        default:
            return null;
        }
    }

    private Long cell2Long(Cell cell)
    {
        Long s = null;
        switch (cell.getCellType())
        {
        case STRING:
            s = Long.parseLong(cell.getStringCellValue());
            break;
        case NUMERIC:
            s = (long) cell.getNumericCellValue();
            break;
        case BOOLEAN:
            break;
        case BLANK:
            break;
        default:
            break;
        }
        return s;
    }

    private Float cell2Float(Cell cell)
    {
        Float s = null;
        switch (cell.getCellType())
        {
        case STRING:
            s = Float.parseFloat(cell.getStringCellValue());
            break;
        case NUMERIC:
            s = (float) cell.getNumericCellValue();
            break;
        case BOOLEAN:
            break;
        case BLANK:
            break;
        default:
            break;
        }
        return s;
    }

    private BigDecimal cell2BigDecimal(Cell cell)
    {
        switch (cell.getCellType())
        {
        case STRING:
            return new BigDecimal(cell.getStringCellValue());
        case NUMERIC:
            return new BigDecimal(cell.getNumericCellValue());
        case BOOLEAN:
            return null;
        case BLANK:
            return null;
        default:
            return null;
        }
    }

    private Double cell2Double(Cell cell)
    {
        switch (cell.getCellType())
        {
        case STRING:
            return Double.parseDouble(cell.getStringCellValue());
        case NUMERIC:
            return cell.getNumericCellValue();
        case BOOLEAN:
            return null;
        case BLANK:
            return null;
        default:
            return null;
        }
    }

    private Boolean cell2Boolean(Cell cell)
    {
        Boolean b = null;
        switch (cell.getCellType())
        {
        case STRING:
            String str = cell.getStringCellValue();
            if ("true".equals(str))
            {
                b = true;
            }
            else if ("false".equals(str))
            {
                b = false;
            }
            break;
        case NUMERIC:
            break;
        case BOOLEAN:
            b = cell.getBooleanCellValue();
        case BLANK:
            break;
        default:
            break;
        }
        return b;
    }

    private Date cell2Date(Cell cell)
    {
        Date date = null;
        switch (cell.getCellType())
        {
        case STRING:
            String strCell = cell.getStringCellValue();
            String dataStr = cell.getStringCellValue();
            try
            {
                // yyyy-mm-dd
                if (strCell.indexOf("-") > -1 && strCell.length() == 10)
                {
                    date = dateFormat.parse(strCell);
                    // yyyy-mm-dd HH:MM:SS
                }
                else if (strCell.indexOf("-") > -1 && strCell.length() == 19)
                {
                    date = dateTimeFormat.parse(dataStr);
                }
                else
                {
                    String extraDateString = getExtraDatePatternString();
                    SimpleDateFormat df = new SimpleDateFormat(extraDateString);
                    date = df.parse(dataStr);
                }
            }
            catch (ParseException e)
            {
                logger.error(e.getMessage(), e);
            }
            return date;
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell))
            {
                date = cell.getDateCellValue();
            }
            return date;
        case BOOLEAN:
            break;
        case BLANK:
            break;
        default:
            break;
        }
        return date;
    }

    private String cell2String(Cell cell)
    {
        String strCell = "";
        switch (cell.getCellType())
        {
        case STRING:
            if (defaultChartSet.toLowerCase().indexOf("utf") > -1
                    && inputFileChartSet.toLowerCase().indexOf("gbk") > -1)
            {
                try
                {
                    strCell = new String(cell.getStringCellValue().getBytes(inputFileChartSet), defaultChartSet);
                }
                catch (UnsupportedEncodingException e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
            else
            {
                strCell = cell.getStringCellValue();
            }
            break;
        case NUMERIC:
            // strCell = String.valueOf(cell.getNumericCellValue());
            // String str = strCell.substring(strCell.length()-2);
            // if(str.equals(".0"))
            // strCell = strCell.substring(0, strCell.length()-2);
            strCell = new BigDecimal(cell.getNumericCellValue()).toPlainString();
            break;
        case BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case BLANK:
            strCell = "";
            break;
        case FORMULA:
            strCell = cell.getRichStringCellValue().getString();
            break;
        default:
            break;
        }
        return strCell.trim();
    }

}
