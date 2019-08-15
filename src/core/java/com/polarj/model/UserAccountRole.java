package com.polarj.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(showDetailFieldName = "code")
@Entity
@Table(name = "useraccountrole",
        indexes = { @Index(columnList = "code,ownerId", name = "UK_UserAccountRole_Code_OwnerId", unique = true) })
public @ToString @EqualsAndHashCode(callSuper = false) class UserAccountRole extends GenericDbInfo implements HasOwner
{
    private static final long serialVersionUID = -4883863886127766993L;

    @FieldMetaData(position = 10, label = "Code", required = true, maxLength = 128)
    @I18nKeyField
    @Column(name = "code", length = 128, nullable = false)
    private @Getter @Setter String code;

    @I18nField
    @FieldMetaData(position = 20, label = "Label",  maxLength = 128)
    @Column(name = "label", length = 128)
    private @Getter @Setter String label;

    @JsonIgnore
    @Column(name = "isDefaultRole")
    private @Getter @Setter Boolean defaultRole;

//    @FieldMetaData(position = 30, label = "Functions", dataType = FieldMetaDataSupportedDataType.ARRAY,
//            managementSeparately = true, enumClass = Functionality.class, hasOwner = true, labelField = "label",
//            multiChoice = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "useraccountrole_functionality",
            joinColumns = @JoinColumn(name = "useraccountroleId",
                    foreignKey = @ForeignKey(name = "FK_useraccountrole_Functionality_useraccountrole_Id")),
            inverseJoinColumns = @JoinColumn(name = "functionalityId",
                    foreignKey = @ForeignKey(name = "FK_useraccountrole_Functionality_functionalty_Id")))
    private @Getter @Setter List<Functionality> functions;

    // 如果用户初始化为这个角色，那么用这个设置初始化用户的设置
   // @FieldMetaData(position = 40, label = "Default Setting", dataType = FieldMetaDataSupportedDataType.OBJECT)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "userAccountConfigId",
            foreignKey = @ForeignKey(name = "FK_UserAccountRole_UserAccountConfig_Id"))
    private @Getter @Setter UserAccountConfig defaultSetting;

    @ManyToOne
    @JoinColumn(name = "ownerId", foreignKey = @ForeignKey(name = "fk_useraccountrole_useraccount_ownerid"),
            referencedColumnName = "id")
    private @Getter @Setter UserAccount owner;

    public void addFunction(Functionality function)
    {
        addElementIntoList(functions, "functions", function);
    }
}
