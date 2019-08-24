package com.polarj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import com.polarj.model.enumeration.SupportedLanguage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "usersetting")
public @ToString @EqualsAndHashCode(callSuper = false) class UserAccountConfig extends GenericDbInfo
        implements HasFieldInJson
{
    private static final long serialVersionUID = 7244571465958582350L;

    // 界面语言
    @FieldMetaData(position = 10, label = "View Language", required = true, enumClass = SupportedLanguage.class,
            minLength = 5, maxLength = 5)
    @Column(name = "viewLang", length = 16, nullable = true, columnDefinition = "char(16) default 'zh-cn'")
    private @Getter @Setter String viewLang;

    // 工作语言：第三方接口，打印的文件等
    @FieldMetaData(position = 20, label = "Work Language", required = true, enumClass = SupportedLanguage.class,
            minLength = 5, maxLength = 5)
    @Column(name = "workLang", length = 16, nullable = true, columnDefinition = "char(16) default 'en-us'")
    private @Getter @Setter String workLang;

    // 顶层中的curPageSize是当前的每次读取数量
    @FieldMetaData(position = 30, label = "Page Size", dataType = FieldMetaDataSupportedDataType.NUMBER,
            required = true, minVal = "10", maxVal = "100")
    @Column(name = "pageSize", nullable = true, columnDefinition = "integer default 10")
    private @Getter @Setter Integer pageSize;

    // ques:这个字段应该是用于下载报表时使用的隐藏对应字段,不进行下载?
    // ques:看代码ModelController 的 generateFieldNameAndHeaderMappingFrom方法
    // 保存为： {"模型名":"字段名,字段名,,...","模型名":"字段名,字段名,,..."}的格式,json对象格式
    // {"DeliveryOrder":"orderNum,sellerName,"}
    @FieldMetaData(position = 40, label = "Hidden Fields", maxLength = 1024)
    @Column(name = "hiddenFields", length = 1024)
    private @Getter @Setter String hiddenFields;

    // 添加一个字段用于保存前端个人呈现model字段的配置数据
    // 保存为： {"模型名":"字段名,字段名,,...","模型名":"字段名,字段名,,..."}的格式,json对象格式
    // {"DeliveryOrder":"orderNum,sellerName,"}
    @FieldMetaData(position = 40, label = "Table Hidden Fields", maxLength = 1024)
    @Column(name = "tableHiddenFields", length = 1024)
    private @Getter @Setter String tableHiddenFields;

    // 登录到系统之后的第一个页面，对应前端的某个路由路径
    // QUES：应该是在一个列表中选择一个
    @FieldMetaData(position = 50, label = "First Page URL", maxLength = 512)
    @Column(name = "firstPageUrl", length = 512)
    private @Getter @Setter String firstPageUrl;

    public void copy(UserAccountConfig newValue)
    {
        if (newValue != null)
        {
            this.viewLang = newValue.viewLang;
            this.workLang = newValue.workLang;
            this.pageSize = newValue.pageSize;
            this.hiddenFields = newValue.hiddenFields;
            this.firstPageUrl = newValue.firstPageUrl;
        }
    }
}
