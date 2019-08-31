package com.polarj.codegeneration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 用于生成与业务模型相关的Spring-Data的基本代码。
// 生成代码时，主要是要修改modelPackage和srcFolder俩个的值
// 注意testTask中使用的generateDatabaseCode，没有参数时，是生成源代码
// 文件夹所有标注为JPA实体的模型的相关代码
public class GenerateModelRelatedBackendCode
{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // ****************************需要修改的部分******************************
    private String srcFolder = getBasePath() + "/src/youjia/java/";

    private String modelPackage = "com.youjia.model";

    private String modelName = "RentBill";
    // *********************************************************************

    public static void main(String[] args)
    {
        GenerateModelRelatedBackendCode tool = new GenerateModelRelatedBackendCode();
        tool.exec();
    }

    protected void exec()
    {
        List<String> modelNames = new ArrayList<String>();

        if (modelName != null && modelName.length() > 0)
        {
            modelNames.add(modelName);
        }
        else
        {
            modelNames.addAll(fetchFilesFromModelFolder());
        }
        if (modelNames.size() == 0)
        {
            logger.info("No model under: " + srcFolder + modelPackage);
            return;
        }

        for (String mName : modelNames)
        {
            logger.info("Will generate {}'s Repos, Service, ServiceImpl, Controller code.", mName);
            Map<String, String> codeTypes = new HashMap<>();
            codeTypes.put("Repos", "repository");
            codeTypes.put("Service", "service");
            codeTypes.put("ServiceImpl", "service/impl");
            codeTypes.put("Controller", "controller");
            for (String codeType : codeTypes.keySet())
            {
                generateCodeByTemplate(mName, codeType, codeTypes.get(codeType));
            }

        }
    }

    private void generateCodeByTemplate(String modelName, String codeType, String codePath)
    {

        String folder = srcFolder + modelPackage.replaceAll("\\.", "/") + "/" + codePath;
        File dir = new File(folder);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        String fileName = folder + "/" + modelName + codeType + ".java";
        File f = new File(fileName);
        if (f.exists())
        {
            logger.info(modelName + "'s service implementation exists.");
            return;
        }

        String templateFileContent = null;
        try
        {
            templateFileContent = getTemplateFileContent(codeType);
            templateFileContent = templateFileContent.replaceAll("\\{modelName\\}", modelName);
            templateFileContent = templateFileContent.replaceAll("\\{modelPackageName\\}", modelPackage);
            templateFileContent = templateFileContent.replaceAll("\\{lowercaseModelName\\}", modelName.toLowerCase());
            OutputStream fos = new FileOutputStream(fileName);
            fos.write(templateFileContent.getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    private String getTemplateFileContent(String codeType) throws Exception
    {
        String templateFile = "model." + codeType.toLowerCase() + ".template.txt";

        StringBuilder templateStr = new StringBuilder();
        InputStream fr = new FileInputStream(getInitDataPath() + templateFile);
        byte[] b = new byte[4096];
        fr.read(b);
        fr.close();
        for (int i = 0; i < b.length && b[i] != 0; i++)
        {
            templateStr.append((char) b[i]);
        }
        logger.info(templateStr.length() + "");
        String fileContent = templateStr.toString();

        return fileContent;
    }

    private List<String> fetchFilesFromModelFolder()
    {
        String folder = srcFolder + modelPackage.replaceAll("\\.", "/");
        List<String> res = new ArrayList<String>();
        File modelDir = new File(folder);
        if (!modelDir.exists())
        {
            logger.info("No model folder: " + folder);
            return res;
        }
        File[] files = modelDir.listFiles();
        if (files == null || files.length == 0)
        {
            logger.info("No model under: " + folder);
            return res;
        }
        for (File file : files)
        {
            if (file.isFile())
            {
                String fileName = file.getName();
                res.add(fileName.substring(0, fileName.indexOf(".")));
            }
        }
        return res;
    }

    private String getInitDataPath()
    {
        return getBasePath() + "/src/main/initdata/";
    }

    private String getBasePath()
    {
        return System.getProperty("user.dir");
    }

}
