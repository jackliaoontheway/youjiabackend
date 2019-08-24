package com.polarj.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import com.polarj.model.enumeration.SearchType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "Model Specification", searchField = "className", addible = false, uploadable = false)
@Entity
@Table(name = "modelspec", indexes = { @Index(columnList = "className", name = "UK_ModelSpecification_className", unique = true) })
// 用于描述业务模型的一些属性
public @ToString @EqualsAndHashCode(callSuper = false) class ModelSpecification extends GenericDbInfo
{
    public static ModelSpecification newInstance(String clazzName, String label)
    {
        ModelSpecification res = new ModelSpecification();
        res.className = clazzName;
        res.iconName = "";
        res.label = label;
        res.searchField = "";
        res.updatable = true;
        res.deletable = true;
        res.cloneable = true;
        res.batchDeletable = true;
        res.addible = true;
        res.downloadable = true;
        res.uploadable = true;
        res.level = 1;
        res.maximumAmountForDownload = 50;
        res.showDetailFieldName = "";
        res.indexField = "";
        res.indexSortDesc = false;
        res.tabField = null;
        res.tabValues = null;
        return res;
    }

    public static ModelSpecification newInstance(String clazzName)
    {
        return newInstance(clazzName, "");
    }

    public static ModelSpecification generateFromClassWithModelMetaDataAnnotation(String clazzName, ModelMetaData mMeta)
    {
        ModelSpecification res = new ModelSpecification();

        res.setAddible(mMeta.addible());
        res.setBatchDeletable(mMeta.batchDeletable());
        res.setClassName(clazzName);
        res.setDeletable(mMeta.deletable());
        res.setDownloadable(mMeta.downloadable());
        res.setLevel(mMeta.level());
        res.setLabel(mMeta.label());
        res.setIconName(mMeta.iconName());
        res.setSearchField(mMeta.searchField());
        res.setUpdatable(mMeta.updatable());
        res.setUploadable(mMeta.uploadable());
        res.setShowDetailFieldName(mMeta.showDetailFieldName());
        res.maximumAmountForDownload = 50;
        res.setIndexField(mMeta.indexField());
        res.setIndexSortDesc(mMeta.sortDesc());

        if (mMeta.tabValues() == null || mMeta.tabValues().length == 0)
        {
            res.setTabField(null);
            res.setTabValues(null);
        }
        else
        {
            res.setTabField(mMeta.tabField());
            String vs = "";
            for (String s : mMeta.tabValues())
            {
                vs = s + ";";
            }
            res.setTabValues(vs);
        }
        return res;
    }
    public static ModelSpecification generateFromClassWithModelMetaDataAnnotation(Class<?> clazz)
    {
        ModelMetaData mMeta = clazz.getAnnotation(ModelMetaData.class);
        if (clazz == null || mMeta == null)
        {
            // 如果传进来的模型类不是被ModelMetaData注解的，不能生成ModelSpecification
            return null;
        }
        return generateFromClassWithModelMetaDataAnnotation(clazz.getName(), mMeta);
    }

    private static final long serialVersionUID = -8156451661279582173L;

    // 模型类的全名
    @FieldMetaData(position = 10, label = "Model Name", required = true, autogenerated = true, sortable = true,
            searchType = SearchType.VALUE)
    @Column(length = 255)
    @I18nKeyField
    private @Getter @Setter String className;

    // 前端图标系统中，用于描述该业务模型的图标名称
    @FieldMetaData(position = 12, label = "Icon Name")
    @Column(length = 32)
    private @Getter @Setter String iconName;

    // 在显示模型数据的时候，用于显示模型的名称
    @FieldMetaData(position = 15, label = "Label", sortable = true, searchType = SearchType.VALUE)
    @Column(length = 255)
    @I18nField
    private @Getter @Setter String label;

    // 该模型的主要搜索字段，在不打开高级搜索的情况下，就搜索该字段
    @FieldMetaData(position = 18, label = "Search Field")
    @Column(length = 255)
    private @Getter @Setter String searchField;

    // 该模型的缺省排序字段
    @FieldMetaData(position = 19, label = "Index Field")
    @Column(length = 255)
    private @Getter @Setter String indexField;

    // 是升序还是降序，缺省是升序
    @FieldMetaData(position = 20, label = "Descent?", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Getter @Setter Boolean indexSortDesc;

    // 该模型是否可以由用户修改记录
    @FieldMetaData(position = 25, label = "Updatable", dataType = FieldMetaDataSupportedDataType.BOOLEAN,
            required = true)
    @Column
    private @Getter @Setter Boolean updatable;

    // 该模型是否可以由用户删除记录
    @FieldMetaData(position = 30, label = "Deletable", dataType = FieldMetaDataSupportedDataType.BOOLEAN,
            required = true)
    @Column
    private @Getter @Setter Boolean deletable;

    // 该模型可以用复制的方式创建新记录
    @FieldMetaData(position = 35, label = "Cloneable", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Getter @Setter Boolean cloneable;

    // 该模型是否可以由用户批量删除
    @FieldMetaData(position = 40, label = "Batch Deletable", dataType = FieldMetaDataSupportedDataType.BOOLEAN,
            required = true)
    @Column
    private @Getter @Setter Boolean batchDeletable;

    // 该模型是否可以由用户添加记录
    @FieldMetaData(position = 50, label = "Addible", dataType = FieldMetaDataSupportedDataType.BOOLEAN, required = true)
    @Column
    private @Getter @Setter Boolean addible;

    // 搜索结果是否可以多选之后下载
    @FieldMetaData(position = 60, label = "Downloadable", dataType = FieldMetaDataSupportedDataType.BOOLEAN,
            required = true)
    @Column
    private @Getter @Setter Boolean downloadable;

    // 用户是否可以通过上传的方式增加记录
    @FieldMetaData(position = 70, label = "Uploadable", dataType = FieldMetaDataSupportedDataType.BOOLEAN,
            required = true)
    @Column
    private @Getter @Setter Boolean uploadable;

    // 该模型的遍历深度，如果其属性也是持久化模型，需要嵌套几次。
    @FieldMetaData(position = 80, label = "Level", required = true, dataType = FieldMetaDataSupportedDataType.NUMBER,
            minVal = "1", maxVal = "4")
    @Column
    private @Getter @Setter Integer level;

    // 允许一次性实时下载的数据总量
    @FieldMetaData(position = 90, label = "Max Amount For Download", dataType = FieldMetaDataSupportedDataType.NUMBER,
            required = true, maxVal = "5000", defaultValue = "1000")
    @Column
    private @Getter @Setter Integer maximumAmountForDownload;

    // 页面上点击哪个字段用于显示详情，该字段不应该能够被隐藏，只有一个字段可以用于显示详情
    @FieldMetaData(position = 100, label = "Field Name for detail link", autogenerated = true)
    @Column(length = 64)
    private @Getter @Setter String showDetailFieldName;

    // QUES:描述一个业务模型会由哪个（些）工作流管理，目前只支持一个
    // 如果支持多个的话，每个工作流名称使用,隔开
    @FieldMetaData(position = 110, label = "Workflow Names", autogenerated = true)
    @Column(length = 512)
    private @Getter @Setter String workflowNames;

    // 前端分类显示的业务模型属性
    @FieldMetaData(position = 120, label = "Tab Field", autogenerated = true)
    @Column(length = 64)
    private @Getter @Setter String tabField;

    // 用于分类的业务模型属性，哪些值用于分类：使用分号分割每个tab的内容，用逗号分割每个tab中包含哪些属性值
    @FieldMetaData(position = 120, label = "Tab Values")
    @Column(length = 128)
    private @Getter @Setter String tabValues;

    @Transient
    private List<String> tabListValues;

    public List<String> getTabListValues()
    {
        if (StringUtils.isEmpty(tabValues))
        {
            return null;
        }
        String[] vs = tabValues.split(";");
        tabListValues = new ArrayList<>();
        for (String s : vs)
        {
            tabListValues.add(s.trim());
        }
        return tabListValues;
    }

    @Transient
    private @Getter @Setter List<FieldSpecification> fieldSpecs;

    @Transient
    private @Getter @Setter List<Functionality> modelFunctionalities;

    @Transient
    private @Getter @Setter List<Functionality> objectFunctionalities;

    public void addFieldSpec(FieldSpecification fieldSpec)
    {
        addElementIntoList(fieldSpecs, "fieldSpecs", fieldSpec);
    }
}
