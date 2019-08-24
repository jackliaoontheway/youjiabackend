package com.polarj.model.component;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polarj.model.annotation.FieldMetaData;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class FileInfo implements Serializable
{
    private static final long serialVersionUID = -1104431603762008906L;

    @FieldMetaData(position=20, label = "File Type")
    @Column(length=16)
    private @Getter @Setter String fileType;

    @Transient
    @JsonIgnore
    private @Getter @Setter byte[] content;

    @FieldMetaData(position=10, label = "File Name")
    @Column(length=255)
    private @Getter @Setter String fileName;

    public FileInfo()
    {

    }

    public FileInfo(String fileType, byte[] content)
    {
        this.fileType = fileType;
        this.content = content;
    }
}
