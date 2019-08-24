package com.hwcargo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData
@Entity
@Table(name = "cartondatapickconfiguration")
public @ToString @EqualsAndHashCode(callSuper = false) class CartonDataPickConfiguration extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = 2392397737980015657L;

    @FieldMetaData(position = 0, label = "plNoCellIndex", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Integer plNoCellIndex;

    @FieldMetaData(position = 10, label = "plNoRowIndex", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Integer plNoRowIndex;

    @FieldMetaData(position = 20, label = "plNoIndicator")
    @Column
    private @Setter @Getter String plNoIndicator;

    @FieldMetaData(position = 30, label = "dataRowIndex", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Integer dataRowIndex;

    @FieldMetaData(position = 40, label = "cartonNoCellIndex", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Integer cartonNoCellIndex;

    @FieldMetaData(position = 50, label = "cartonNoIndicator")
    @Column
    private @Setter @Getter String cartonNoIndicator;

    @FieldMetaData(position = 60, label = "gwCellIndex", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Integer gwCellIndex;

    @FieldMetaData(position = 70, label = "gwIndicator")
    @Column
    private @Setter @Getter String gwIndicator;

    @FieldMetaData(position = 80, label = "sizeCellIndex", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Integer sizeCellIndex;

    @FieldMetaData(position = 90, label = "sizeIndicator")
    @Column
    private @Setter @Getter String sizeIndicator;

    /*
     * 目前所有的volumn 都是size后面一个字段
     * 
     * @FieldMetaData(position = 80, label = "volumeCellIndex", dataType = FieldMetaDataSupportedDataType.NUMBER)
     * 
     * @Column private @Setter @Getter Integer volumeCellIndex;
     * 
     * @FieldMetaData(position = 90, label = "volumeIndicator")
     * 
     * @Column private @Setter @Getter String volumeIndicator;
     */

    @FieldMetaData(position = 100, label = "endRowIndicator")
    @Column
    private @Setter @Getter String endRowIndicator;

}
