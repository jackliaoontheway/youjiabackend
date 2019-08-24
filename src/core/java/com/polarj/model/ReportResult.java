package com.polarj.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.component.ReportFile;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reportresult")
public @ToString @EqualsAndHashCode(callSuper = false) class ReportResult extends GenericDbInfo
{
    private static final long serialVersionUID = -1780499723531438569L;

    @FieldMetaData(position = 10, label = "Report Result File", dataType=FieldMetaDataSupportedDataType.FILE)
    @Embedded
    private @Getter @Setter ReportFile reportFile;

    @FieldMetaData(position = 20, label = "Generated Time", dataType = FieldMetaDataSupportedDataType.DATE,
            formatter = FieldMetaDataSupportedDataType.TIMEFORMAT)
    @Column(length = 255)
    private @Getter @Setter Date generatedTime;

}
