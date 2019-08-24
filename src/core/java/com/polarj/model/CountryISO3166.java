package com.polarj.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.I18nField;
import com.polarj.model.annotation.I18nKeyField;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import com.polarj.model.enumeration.SearchType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(level = 2, uploadable = false)
@Entity
@Table(name = "isocountry", indexes = { @Index(columnList = "code", name = "UK_Country_Code", unique = true) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "className")
// 符合ISO 3166标准的国家列表
public @ToString @EqualsAndHashCode(callSuper = false) class CountryISO3166 extends GenericDbInfo
{
    private static final long serialVersionUID = 3872982101519156400L;

    // 两位国家代码
    @FieldMetaData(position = 10, label = "Code", required = true, minLength = 2, maxLength = 2, sortable = true,
            searchType = SearchType.VALUE)
    @I18nKeyField
    @Column(length = 8)
    private @Getter @Setter String code;

    @FieldMetaData(position = 20, label = "Name", required = true, searchType = SearchType.VALUE)
    @I18nField
    @Column(length = 255)
    private @Getter @Setter String name;

    @FieldMetaData(position = 30, label = "Supported", dataType = FieldMetaDataSupportedDataType.BOOLEAN,
            required = true, sortable = true, searchType = SearchType.VALUE)
    @Column
    private @Getter @Setter Boolean supported;

    // QUES 在级联删除时，没有把orphanRemoval设置为true，就删除不成功，具体原因还没有研究。
    @FieldMetaData(position = 50, label = "States", dataType = FieldMetaDataSupportedDataType.ARRAY, labelField="name")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "countryId", nullable = false, foreignKey = @ForeignKey(name = "FK_Country_State_Id"))
    private @Getter @Setter List<StateISO3166> states;

    public void addNewState(StateISO3166 state)
    {
        addElementIntoList(states, "states", state);
    }

    @FieldMetaData(position = 40, label = "Currency", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = CurrencyISO4217.class, managementSeparately = true, labelField = "name", uniqueField = "code")
    @OneToOne
    @JoinColumn(name = "currencyId", foreignKey = @ForeignKey(name = "FK_Country_Currency_Id"))
    private @Getter @Setter CurrencyISO4217 currency;
}
