package com.polarj.model.component;

import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@Embeddable
public @ToString(callSuper = true) @EqualsAndHashCode(callSuper = true) class PersonalPhoto extends FileInfo
{
    private static final long serialVersionUID = -2247370982979233130L;

    public PersonalPhoto()
    {

    }

    public PersonalPhoto(String fileType, byte[] labelData)
    {
        super(fileType, labelData);
    }


}
