package com.polarj.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * 无层次的组 , 组和用户是多对多的关系
 */
@ModelMetaData(showDetailFieldName = "code")
@Entity
public @ToString @EqualsAndHashCode(callSuper = false) class WorkGroup extends UserAccountGroup
{
    private static final long serialVersionUID = 2910367869818278861L;

    @FieldMetaData(position = 120, label = "User Accounts", dataType = FieldMetaDataSupportedDataType.ARRAY,
            labelField = "loginName", managementSeparately = true, hasDetail = false)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "workgroup_useraccount",
            joinColumns = @JoinColumn(name = "userAccountId",
                    foreignKey = @ForeignKey(name = "fk_useraccount_workgroup_useraccount_id")),
            inverseJoinColumns = @JoinColumn(name = "workgroupId",
                    foreignKey = @ForeignKey(name = "fk_useraccount_workgroup_workgroup_id")))
    private @Setter List<UserAccount> userAccountList;

    public List<UserAccount> getUserAccountList()
    {
        this.userAccountList = addGroupManagerInGroup(userAccountList);
        return this.userAccountList;
    }

}
