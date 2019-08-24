package com.polarj.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户组管理主要是对公司的组织结构的抽象 流程管理 比如审批流程 资源管理 比如平台帐号
 */
@Entity
@Table(name = "useraccountgroup", indexes = {
        @Index(columnList = "classification,ownerId,code", name = "uk_useraccountgroup_ownerId_code", unique = true) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "classification")
public abstract @ToString @EqualsAndHashCode(callSuper = false) class UserAccountGroup extends GenericDbInfo
        implements HasOwner
{
    private static final long serialVersionUID = 7745844951967974243L;

    @FieldMetaData(position = 10, label = "Code", required = true, maxLength = 128)
    @I18nKeyField
    @Column(name = "code", length = 128, nullable = false)
    private @Getter @Setter String code;

    @I18nField
    @FieldMetaData(position = 20, label = "Label", required = true, maxLength = 128)
    @Column(name = "label", length = 128)
    private @Getter @Setter String label;

    @FieldMetaData(position = 30, label = "Position", dataType = FieldMetaDataSupportedDataType.NUMBER, required = true,
            minVal = "1")
    @Column(name = "positionSn", nullable = false)
    private @Setter @Getter Integer positionSn;

    // 组管理员属于某个角色,应该通过角色来标识,可以有多个管理员 默认有一个组管理员
    @FieldMetaData(position = 40, label = "Manager", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = UserAccount.class, hasOwner = true, labelField = "loginName", managementSeparately = true)
    @ManyToOne
    @JoinColumn(name = "groupManagerId", foreignKey = @ForeignKey(name = "fk_useraccountgroup_manager_id"))
    private @Getter @Setter UserAccount groupManager;

    @FieldMetaData(position = 50, label = "Roles", dataType = FieldMetaDataSupportedDataType.ARRAY,
            enumClass = UserAccountRole.class, hasOwner = true, labelField = "label", multiChoice = true,
            managementSeparately = true, uniqueField = "code")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "useraccountgroup_role",
            joinColumns = @JoinColumn(name = "usergroupId",
                    foreignKey = @ForeignKey(name = "fk_useraccountgroup_role_useraccountgroup_id")),
            inverseJoinColumns = @JoinColumn(name = "useraccountroleId",
                    foreignKey = @ForeignKey(name = "fk_useraccountgroup_role_role_id")))
    private @Getter @Setter List<UserAccountRole> roles;

    @ManyToOne
    @JoinColumn(name = "ownerId", foreignKey = @ForeignKey(name = "fk_useraccountgroup_useraccount_ownerid"))
    private @Getter @Setter UserAccount owner;

    protected List<UserAccount> addGroupManagerInGroup(List<UserAccount> userAccountList)
    {
        if (this.groupManager == null)
        {
            return userAccountList;
        }

        if (userAccountList == null)
        {
            userAccountList = new ArrayList<>();
        }

        boolean groupManagerIsExisted = false;
        for (UserAccount userAccount : userAccountList)
        {
            if (userAccount.getId().equals(this.groupManager.getId()))
            {
                groupManagerIsExisted = true;
                break;
            }
        }
        if (!groupManagerIsExisted)
        {
            userAccountList.add(this.groupManager);
        }
        return userAccountList;
    }

}
