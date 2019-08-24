package com.polarj.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;
import com.polarj.model.enumeration.SearchType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(showDetailFieldName = "FilterStrategy")
@Entity
@Table(name = "filterStrategy")
public @ToString @EqualsAndHashCode(callSuper = false) class ModelFilterStrategy extends GenericDbInfo
{
    private static final long serialVersionUID = -1L;

    // field name for both db and model
    @FieldMetaData(position = 10, label = "Strategy Name")
    @Column(length = 32)
    private @Getter @Setter String strategyName;

    // which model in full name has this field
    @FieldMetaData(position = 20, label = "Class Full Name",sortable = true,
            searchType = SearchType.VALUE)
    @Column(length = 220)
    private @Getter @Setter String classFullName;

    @FieldMetaData(position = 210, label = "Filter Items", labelField = "filter Items",
            dataType = FieldMetaDataSupportedDataType.ARRAY)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "filterStrategyId", foreignKey = @ForeignKey(name = "FK_ModelFilter_Strategy_Item_Id"))
    private @Getter @Setter List<ModelFilterStrategyItem> filterItems;

}
