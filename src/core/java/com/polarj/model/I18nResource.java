package com.polarj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.SupportedLanguage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(showDetailFieldName="i18nKey")
@Entity
@Table(name = "resourcei18n",
        indexes = { @Index(columnList = "i18nKey, languageId", name = "UK_i18nKey_languageId", unique = true) })
public @ToString @EqualsAndHashCode(callSuper = false) class I18nResource extends GenericDbInfo
{
    private static final long serialVersionUID = 9002101042530691502L;

    // 资源代码
    // 数据库字段：对应模型的类全名+字段名
    // 数据库字段的值： 对应模型的类全名+字段名+模型的唯一标识值
    @FieldMetaData(required = true, label = "Key", position = 10)
    @Column(name = "i18nKey", length = 511, nullable = false)
    private @Setter @Getter String i18nKey;

    // 下拉列表的选择en-us, zh-cn,等
    @FieldMetaData(required = true, label = "Language", enumClass = SupportedLanguage.class, position = 20)
    @Column(name = "languageId", length = 128, nullable = false)
    private @Setter @Getter String languageId;

    // 对应语言的显示文本
    @FieldMetaData(required = true, label = "Value", position = 30)
    @Column(name = "i18nValue", length = 8192, nullable = false)
    private @Setter @Getter String i18nValue;
}
