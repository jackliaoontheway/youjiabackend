package com.testing.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.polarj.model.UserAccount;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.InJsonField;
import com.polarj.model.annotation.ModelMetaDataLabel;

import lombok.Getter;
import lombok.Setter;

@ModelMetaDataLabel(label="Sub Domain User Account")
public class SubDomainUser extends UserAccount
{
    private static final long serialVersionUID = -8102728678697382506L;

    @FieldMetaData(label="Code in Sub Domain", position=20)
    @JsonView(InJsonField.class)
    private @Getter @Setter String code;

    @FieldMetaData(label="Name in Sub Domain", position=30)
    @JsonView(InJsonField.class)
    private @Getter @Setter String name;
}
