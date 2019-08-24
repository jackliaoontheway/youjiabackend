package com.polarj;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polarj.common.CommonConstant;
import com.polarj.common.utility.csv.CsvReader;

import lombok.Getter;

abstract public class TestBase
{
    protected final Logger logger;

    private @Getter(value = lombok.AccessLevel.PROTECTED) String basePath = System.getProperty("user.dir");

    private @Getter(value = lombok.AccessLevel.PROTECTED) String testDataPath = basePath + "/testdata/";

    private @Getter(value = lombok.AccessLevel.PROTECTED) String initDataPath = basePath + "/src/main/initdata/";

    protected final Integer operId = CommonConstant.systemUserAccountId;
    
    protected final String languageId = CommonConstant.defaultSystemLanguage;//"zh-cn";

    public TestBase()
    {
        logger = LoggerFactory.getLogger(this.getClass());
    }
    
    protected List<String[]> readContentFromCsvFile(String csvFileName)
    {
        String fileName = getInitDataPath() + csvFileName;
        List<String[]> contents = new ArrayList<String[]>();
        try
        {
            CsvReader csvRd = new CsvReader(fileName);
            csvRd.setTextQualifier('\'');
            while (csvRd.readRecord())
            {
                String[] row = csvRd.getValues();
                contents.add(row);
            }
            csvRd.close();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return contents;
    }

}
