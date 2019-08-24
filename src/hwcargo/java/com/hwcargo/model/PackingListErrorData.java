package com.hwcargo.model;

import com.polarj.common.utility.report.FieldNameAndHeaderMapping;

import lombok.Getter;
import lombok.Setter;

public class PackingListErrorData
{
    public static FieldNameAndHeaderMapping getFieldNameAndHeaderMapping()
    {
        FieldNameAndHeaderMapping fieldNameAndHeaderMapping = new FieldNameAndHeaderMapping();
        fieldNameAndHeaderMapping.addFieldNameAndHeaderPair("fileName", "文件名");
        fieldNameAndHeaderMapping.addFieldNameAndHeaderPair("errorMsg", "错误信息");

        return fieldNameAndHeaderMapping;
    }

    /**
     * 序号
     */
    private @Setter @Getter String fileName;

    /**
     * 分单号
     */
    private @Setter @Getter String errorMsg;

}
