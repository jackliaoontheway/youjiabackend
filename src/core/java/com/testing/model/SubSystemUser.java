package com.testing.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.polarj.model.UserAccount;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.InJsonField;
import com.polarj.model.annotation.ModelMetaDataLabel;

import lombok.Getter;
import lombok.Setter;

@ModelMetaDataLabel(label="Sub System User Account")
public class SubSystemUser extends UserAccount
{
    private static final long serialVersionUID = 8661136425421499963L;

    @FieldMetaData(label = "Sub System Name", position = 20)
    @JsonView(InJsonField.class)
    private @Getter @Setter String systemName;

    @FieldMetaData(label = "Memo for Sub System", position = 20)
    @JsonView(InJsonField.class)
    private @Getter @Setter String memo;
}
