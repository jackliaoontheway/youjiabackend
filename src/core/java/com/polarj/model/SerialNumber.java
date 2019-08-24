package com.polarj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "serialnumber",
        indexes = { @Index(columnList = "moduleCode", name = "UK_SerialNumber_moduleCode", unique = true) })
public @ToString @EqualsAndHashCode(callSuper = false) class SerialNumber extends GenericDbInfo
{
    private static final long serialVersionUID = -2699354538967511959L;

    // 使用序列号的模块代码，全局唯一
    @Column(length = 32)
    @I18nKeyField
    private @Getter @Setter String moduleCode;

    // 使用序列号的模块说明，需要国际化
    @Column(length = 512)
    @I18nField
    private @Getter @Setter String description;

    // 序列号的模式，以字母串开头的可以包含${DATE}和0的字符串
    // 例如：SN${DATE}0000，生成的序列号就是：SN201701010001-SN201701019999
    @Column(length = 128)
    private @Getter @Setter String snPattern;

    // 当前可能被使用过/已经缓冲了的最后一个序列号
    @Column
    private @Getter @Setter Long currentSn;

    // 最后一次生成的序列号中的日期字符串（如果有的话）
    @Column(length = 64)
    private @Getter @Setter String lastDateInSn;

    // 预生成序列号存放到缓存的个数
    @Column
    private @Getter @Setter Integer inCacheSnAmount;
}
