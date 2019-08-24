package com.polarj.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(addible = false, updatable = false, deletable = false, uploadable = false)
@Entity
@Table(name = "loginaudit")
public @ToString @EqualsAndHashCode(callSuper = false) class LoginAudit extends GenericDbInfo
{
    private static final long serialVersionUID = -4835152835304402294L;

    @FieldMetaData(required = true, label = "IP Address", position = 20)
    @Column(name = "ipAddress", length = 64, nullable = false)
    private @Setter @Getter String ipAddress;

    @Column(name = "jsessionId", length = 64, nullable = false)
    private @Setter @Getter String jsessionId;

    @FieldMetaData(required = true, label = "Login Name", position = 10)
    @Column(name = "loginName", length = 128, nullable = false)
    private @Setter @Getter String loginName;

    @Column(name = "usedPasswordHash", length = 128, nullable = false)
    private @Setter @Getter String usedPasswordHash;

    @FieldMetaData(required = true, label = "Login Time", dataType = FieldMetaDataSupportedDataType.DATE,
            formatter = FieldMetaDataSupportedDataType.TIMEFORMAT, position = 30)
    @Column(name = "loginTime", nullable = false)
    private @Setter @Getter Date loginTime;

    @FieldMetaData(required = true, label = "Logout Time", dataType = FieldMetaDataSupportedDataType.DATE,
            formatter = FieldMetaDataSupportedDataType.TIMEFORMAT, position = 40)
    @Column(name = "logoutTime", nullable = true)
    private @Setter @Getter Date logoutTime;
}
