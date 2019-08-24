package com.polarj.common.web.model;

import com.polarj.common.ResponseBase;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public @ToString @EqualsAndHashCode(callSuper = false) class ServerResponse<T> extends ResponseBase<T>
{
    private @Getter @Setter String nonceToken; // against CRSF

    // 对于分页数据的返回， 下列数据描述分页情况
    // 记录总数
    private @Getter @Setter long totalRecords;

    // 总页数
    private @Getter @Setter long totalPages;

    // 当前是第几页（0开始）
    private @Getter @Setter long currentPageIndex;

    // 每页有多少数据（该数据和用户的配置相对应）
    private @Getter @Setter long pageSize;
    // 对于分页数据的返回， 以上数据描述分页情况

    public void addError(WebErrorStatus err)
    {
        super.addStatus(err);
    }

    public void addInfo(WebSuccessStatus msg)
    {
        super.addStatus(msg);
    }
}
