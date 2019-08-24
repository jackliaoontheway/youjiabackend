package com.polarj.codegeneration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polarj.common.utility.FieldValueUtil;
import com.polarj.model.GenericDbInfo;

// 用于生成业务模型的前端基本代码
public class GenerateModelRelatedFrontendCode
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String frontSrcDir = System.getProperty("user.dir") + "/../frontend/src/app/";

    private String modelPath = "model/";

    private String modelComponentPath = modelPath + "model-component/";

    private String modelComponentTemplate = "model-component.template.txt";

    private String mainModulePath = "main/";

    private String modelPagePath = mainModulePath + "content/model-page/";

    private String modelPageTemplate = "model-page.template.txt";

    private String mainModuleName = frontSrcDir + mainModulePath + "main.module.ts";

    private String mainRoutingName = frontSrcDir + mainModulePath + "main-routing.module.ts";

    private String modelPageImportPositionFlag = "Generated Model Page Begin";

    private String modelPageDeclarePositionFlag = "Declare Model Page Begin";

    private String modelCompImportPositionFlag = "Generated Model Component Begin";

    private String modelCompDeclarePositionFlag = "Declare Model Component Begin";

    private String routePositionFlag = "Generated Routes Begin";
    
    public static void main(String[] args)
    {
        GenerateModelRelatedFrontendCode tester = new GenerateModelRelatedFrontendCode();
        try
        {
            tester.exec();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void exec() throws Exception
    {
        String modelName = "ManifestInfo";
        String modelPackageName = "com.ecomerp.express.model";
        List<String> elmModels = new ArrayList<String>();
        generateFrontModelCode(modelPackageName + ".", modelName, elmModels);
        generateFrontModelComponentCode(modelName);
        generateFrontModelPageCode(modelName);
    }

    void generateFrontModelPageCode(String backendModelName) throws Exception
    {
        // 使用模板生成前端的模型管理页面代码
        generateFrontModelRelatedCode(frontSrcDir + modelPagePath, backendModelName, modelPageTemplate,
                ".component.ts");
        // 加入到前端的模块代码中
        updateMainRelatedCode(mainModuleName, backendModelName, true, false);
        // 加入到前端的路由代码中
        updateMainRelatedCode(mainRoutingName, backendModelName, true, false);
    }

    void generateFrontModelComponentCode(String backendModelName) throws Exception
    {
        // 使用模板生成前端的模型组件代码
        generateFrontModelRelatedCode(frontSrcDir + modelComponentPath, backendModelName, modelComponentTemplate,
                "-model.component.ts");
        // 把生成的模型组件加入到前端的模块代码中
        updateMainRelatedCode(mainModuleName, backendModelName, false, true);
    }

    void generateFrontModelCode(String modelPackageName, String backendModelName, List<String> elmModels)
            throws Exception
    {
        // 使用后端的模型代码，生成前端的模型代码
        // String frontSrcDir = "/home/ping/appdata/gitrepos/dragonparcel/";
        List<String> elmModelFullNames = new ArrayList<String>();
        List<String> elmModelsForThisModel = new ArrayList<String>();
        Class<?> clazz = Class.forName(modelPackageName + backendModelName);
        if (clazz == null)
        {
            logger.error("can not generate model class for: " + backendModelName);
            return;
        }
        Field[] fields = getAllFields(clazz, logger);
        if (fields == null || fields.length == 0)
        {
            logger.info("no any fields in this model: " + backendModelName + ". must have something wrong");
            return;
        }
        // 先把模型中的所有类型也是模型的属性找出来，需要先生成代码
        for (Field field : fields)
        {
            Class<?> fieldType = FieldValueUtil.getTypeOfField(field, logger);
            if (GenericDbInfo.class.isAssignableFrom(fieldType) || fieldType.isAnnotationPresent(Embeddable.class))
            {
                String modelName = fieldType.getSimpleName();
                if (!elmModelsForThisModel.contains(modelName))
                {
                    elmModelsForThisModel.add(modelName);
                    if (!elmModels.contains(modelName))
                    {
                        elmModelFullNames.add(fieldType.getName());
                        elmModels.add(modelName);
                    }
                }
            }
        }

        // 1. 先生成属性为类类型的代码
        for (String elmModel : elmModelFullNames)
        {
            if (!elmModel.endsWith("UserAccount"))
            {
                generateFrontModelCode(elmModel.substring(0, elmModel.lastIndexOf(".") + 1),
                        elmModel.substring(elmModel.lastIndexOf(".") + 1), elmModels);
            }
        }
        // 2. 按行生成前端模型代码
        FileWriter modelFile = new FileWriter(frontSrcDir + modelPath + backendModelName.toLowerCase() + ".model.ts");
        String line = "import { BaseModel } from './base.model';\n";
        modelFile.write(line);
        for (String elmModel : elmModelsForThisModel)
        {
            if (!elmModel.equalsIgnoreCase("UserAccount"))
            {
                line = "import { " + elmModel + "Model } from './" + elmModel.toLowerCase() + ".model';\n";
            }
            else
            {
                line = "import { UseraccountModel } from './useraccount.model';\n";
            }
            modelFile.write(line);
        }
        modelFile.write("\n");
        line = "export class " + backendModelName + "Model extends BaseModel {\n";
        modelFile.write(line);
        for (Field field : fields)
        {
            if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers()))
            {
                // 忽略常量
                continue;
            }
            if (field.isAnnotationPresent(JsonIgnore.class))
            {
                // 前端不需要的属性
                continue;
            }
            Class<?> fieldType = FieldValueUtil.getTypeOfField(field, logger);
            if (fieldType.isPrimitive())
            {
                if (fieldType.getSimpleName().equalsIgnoreCase("boolean"))
                {
                    line = "  " + field.getName() + ": " + "boolean;\n";
                }
                else
                {
                    line = "  " + field.getName() + ": " + "number;\n";
                }
            }
            else if (Number.class.isAssignableFrom(fieldType))
            {
                line = "  " + field.getName() + ": " + "number;\n";
            }
            else if (fieldType.getSimpleName().equalsIgnoreCase("boolean"))
            {
                line = "  " + field.getName() + ": " + "boolean;\n";
            }
            else if (fieldType.getSimpleName().equalsIgnoreCase("String"))
            {
                line = "  " + field.getName() + ": " + "string;\n";
            }
            else if (fieldType.getSimpleName().equalsIgnoreCase("date"))
            {
                line = "  " + field.getName() + ": " + "Date;\n";
            }
            else if (GenericDbInfo.class.isAssignableFrom(fieldType) || fieldType.isAnnotationPresent(Embeddable.class))
            {
                if (fieldType.getSimpleName().equalsIgnoreCase("UserAccount"))
                {
                    line = "  " + field.getName() + ": " + "Useraccount";
                }
                else
                {
                    line = "  " + field.getName() + ": " + fieldType.getSimpleName();
                }
                line = line + "Model";
                if (field.getType().getSimpleName().equalsIgnoreCase("list"))
                {
                    line = line + "[]";
                }
                line = line + ";\n";
            }
            else
            {
                line = "//  " + field.getName() + ": " + "unkonw;\n";
            }
            modelFile.write(line);
        }
        modelFile.write("}\n");
        modelFile.close();
    }

    private Field[] getAllFields(Class<?> clazz, Logger logger)
    {
        List<Field> fields = new ArrayList<Field>();

        while (!clazz.getSimpleName().equalsIgnoreCase("GenericDbInfo")
                && !clazz.getSimpleName().equalsIgnoreCase("Object"))
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

    private void generateFrontModelRelatedCode(String path, String backendModelName, String templateFile,
            String tsFileType) throws Exception
    {
        logger.info("Will generate: " + backendModelName.toLowerCase() + tsFileType);
        String modelRelatedCodeName = path + backendModelName.toLowerCase() + tsFileType;
        StringBuilder templateStr = new StringBuilder();
        InputStream fr = new FileInputStream(getInitDataPath() + templateFile);
        byte[] b = new byte[4096];
        fr.read(b);
        fr.close();
        for (int i = 0; i < b.length && b[i] != 0; i++)
        {
            templateStr.append((char) b[i]);
        }
        String fileContent = templateStr.toString();
        fileContent = fileContent.replaceAll("\\{modelName\\}", backendModelName);
        fileContent = fileContent.replaceAll("\\{lowercaseModelName\\}", backendModelName.toLowerCase());
        OutputStream fos = new FileOutputStream(modelRelatedCodeName);
        fos.write(fileContent.getBytes());
        fos.close();
    }

    private String getInitDataPath()
    {
        // TODO Auto-generated method stub
        return null;
    }

    private void updateMainRelatedCode(String tsFileName, String backendModelName, boolean isPageCode,
            boolean isCompCode) throws Exception
    {
        logger.info("Will update " + tsFileName + ", to add " + backendModelName + "'s component.");
        RandomAccessFile raf = new RandomAccessFile(tsFileName, "r");
        List<String> fileContents = new ArrayList<String>();
        String line = raf.readLine();
        while (line != null)
        {
            fileContents.add(line);

            if (isCompCode)
            {
                if (line.contains(modelCompImportPositionFlag))
                {
                    String newLine = "import { " + backendModelName + "ModelComponent } from '../model/model-component/"
                            + backendModelName.toLowerCase() + "-model.component';";
                    fileContents.add(newLine);
                }
                if (line.contains(modelCompDeclarePositionFlag))
                {
                    String newLine = "        " + backendModelName + "ModelComponent,";
                    fileContents.add(newLine);
                }
            }
            if (isPageCode)
            {
                if (line.contains(modelPageImportPositionFlag))
                {
                    String newLine = "import { " + backendModelName + "Component } from './content/model-page/"
                            + backendModelName.toLowerCase() + ".component';";
                    fileContents.add(newLine);
                }
                if (line.contains(modelPageDeclarePositionFlag))
                {
                    String newLine = "        " + backendModelName + "Component,";
                    fileContents.add(newLine);
                }
                if (line.contains(routePositionFlag))
                {
                    String newLine = "      { path: '" + backendModelName.toLowerCase() + "', component: "
                            + backendModelName + "Component, },";
                    fileContents.add(newLine);
                }
            }
            line = raf.readLine();
        }
        raf.close();

        raf = new RandomAccessFile(tsFileName, "rw");
        for (String s : fileContents)
        {
            raf.writeBytes(s);
            raf.writeBytes("\n");
        }
        raf.close();
    }

    

}
