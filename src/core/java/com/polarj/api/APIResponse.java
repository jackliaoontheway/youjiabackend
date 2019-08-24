package com.polarj.api;

import com.polarj.common.ResponseBase;

import lombok.Getter;
import lombok.Setter;

public class APIResponse<T> extends ResponseBase<T>
{
    // 返回该值的被调用方法，由于数据流量限制，还需要再被调用
    private @Getter @Setter boolean needInvokeMore;
}
