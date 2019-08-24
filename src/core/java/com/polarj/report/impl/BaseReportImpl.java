package com.polarj.report.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseReportImpl
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected List<String> readFile(String path)
    {
        if (path == null || path.length() == 0)
        {
            return null;
        }
        File file = new File(path);
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        try
        {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            String str = null;
            while ((str = br.readLine()) != null)
            {
                list.add(str);
            }
            return list;
        }
        catch (Exception e)
        {
            logger.error("readFile error"+e.getMessage(), e);
        }
        finally
        {
            try
            {
                br.close();
            }
            catch (IOException e)
            {
                logger.error("BufferedReader is not close", e);
            }
        }
        return null;
    }
}
