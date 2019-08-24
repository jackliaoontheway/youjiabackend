package com.polarj.model.component;

import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@Embeddable
public @ToString(callSuper = true) @EqualsAndHashCode(callSuper = true) class ReportFile extends FileInfo
{
    private static final long serialVersionUID = 2322629103746083909L;

    public ReportFile()
    {

    }

    public ReportFile(String fileType, byte[] labelData)
    {
        super(fileType, labelData);
    }

}
