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

@ModelMetaData(searchField = "mawb")
@Entity
@Table(name = "manifestdata")
public @ToString @EqualsAndHashCode(callSuper = false) class ManifestData extends GenericDbInfo
{
    /**
     * 
     */
    private static final long serialVersionUID = -5555102702241296221L;

    /**
     * 序号
     */
    @FieldMetaData(position = 0, label = "Serial Number", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter int serialNum;

    /**
     * 主单号
     */
    @FieldMetaData(position = 10, label = "Mawb")
    @Column(length = 255)
    private @Setter @Getter String mawb;

    /**
     * 分单号
     */
    @FieldMetaData(position = 10, label = "Hawb")
    @Column(length = 255)
    private @Setter @Getter String hawb;

    /**
     * 888单号
     */
    @FieldMetaData(position = 20, label = "888 Number")
    @Column(length = 255)
    private @Setter @Getter String three8Number;

    @FieldMetaData(position = 25, label = "FlightNo")
    @Column(length = 255)
    private @Setter @Getter String flightNo;

    @FieldMetaData(position = 28, label = "DeadLine")
    @Column(length = 255)
    private @Setter @Getter String deadLine;

    @FieldMetaData(position = 29, label = "Destination")
    @Column(length = 1024)
    private @Setter @Getter String destination;

    /**
     * 件数
     */
    @FieldMetaData(position = 30, label = "Qty", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter int qty;

    /**
     * 重量
     */
    @FieldMetaData(position = 40, label = "Weight", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter double weight;

    /**
     * 品名
     */
    @FieldMetaData(position = 50, label = "Product Name")
    @Column(length = 1024)
    private @Setter @Getter String productName;

    /**
     * 尺寸(CM)
     */
    @FieldMetaData(position = 60, label = "Dimensions")
    @Column(length = 2048)
    private @Setter @Getter String dimensions;

    /**
     * 体积
     */
    @FieldMetaData(position = 65, label = "Volumn")
    @Column
    private @Setter @Getter double volumn;

    /**
     * 是否含电池
     */
    @FieldMetaData(position = 70, label = "Electr Innered")
    @Column
    private @Setter @Getter String electrInnered;

    /**
     * 箱单号
     */
    @FieldMetaData(position = 80, label = "PackingListNo")
    @Column(length = 2048)
    private @Setter @Getter String packingListNo;

    /**
     * ETD
     */
    @FieldMetaData(position = 90, label = "DeadLine")
    @Column(length = 255)
    private @Setter @Getter String etd;

    /**
     * ETA
     */
    @FieldMetaData(position = 100, label = "Destination")
    @Column(length = 1024)
    private @Setter @Getter String eta;

    /**
     * 总件数
     */
    @FieldMetaData(position = 110, label = "Qty total", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Integer qtyTotal;

    /**
     * 总重量
     */
    @FieldMetaData(position = 120, label = "Weight total", dataType = FieldMetaDataSupportedDataType.NUMBER)
    @Column
    private @Setter @Getter Double weightTotal;

}
