package com.polarj.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 有层次结构的组 组和用户是一对多的关系 用户不能在多个组
 */
@ModelMetaData(showDetailFieldName = "code")
@Entity
public @ToString @EqualsAndHashCode(callSuper = false) class DivisionGroup extends UserAccountGroup
{
    private static final long serialVersionUID = -712117544915485166L;

    @FieldMetaData(position = 100, label = "Parent Division", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = DivisionGroup.class, hasOwner = true, labelField = "label", managementSeparately = true)
    @ManyToOne
    @JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "fk_subdivision_division_id"),
            referencedColumnName = "id")
    private @Getter @Setter DivisionGroup parentDivision;

    @Transient
    private @Setter @Getter List<DivisionGroup> subDivisions;

    @FieldMetaData(position = 120, label = "User Accounts", dataType = FieldMetaDataSupportedDataType.ARRAY,
            labelField = "loginName", managementSeparately = true, hasDetail = false)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "divisionGroupId", foreignKey = @ForeignKey(name = "fk_useraccount_division_group_id"))
    private @Setter List<UserAccount> userAccountList;
    
    public List<UserAccount> getUserAccountList()
    {
        this.userAccountList = addGroupManagerInGroup(userAccountList);
        return this.userAccountList;
    }
}
