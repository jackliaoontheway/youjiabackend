package com.polarj.model.component;

import lombok.Getter;
import lombok.Setter;

public class SearchScope
{
    // 属性名称
    private @Getter @Setter String fieldName;

    // 属性的类型
    private @Getter @Setter Class<?> typeClazz;

    // 开始值
    private @Getter @Setter String fromValue;

    // 结束值
    private @Getter @Setter String toValue;
}
