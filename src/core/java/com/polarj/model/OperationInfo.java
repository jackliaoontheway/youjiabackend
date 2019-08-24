package com.polarj.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.Getter;
import lombok.Setter;

// 记录系统中业务模型的操作信息，业务模型被做了一些什么操作，用做系统跟踪
// 数据量大，只有新增，需要经常被查询。
// 前端只能浏览，不能做任何操作，所有的操作都是后台进行

@ModelMetaData(updatable = false, deletable = false, batchDeletable = false, addible = false, uploadable = false)
@Entity
@Table(name = "operation")
public class OperationInfo extends GenericDbInfo
{
    private static final long serialVersionUID = 8101259175708381146L;

    // 业务模型的类名
    @FieldMetaData(required = true, label = "Model Name", position = 10)
    @Column(name = "modelName", length = 64, nullable = false)
    private @Getter @Setter String modelName;

    // 业务模型的数据库ID
    @FieldMetaData(required = true, label = "Model ID", position = 20)
    @Column(name = "instanceId", nullable = false)
    private @Getter @Setter Integer instanceId;

    // 业务模型的操作
    @FieldMetaData(required = true, label = "Operation", position = 30)
    @Column(name = "operation", length = 64, nullable = false)
    private @Getter @Setter String operation;

    // 业务模型的操作说明
    @FieldMetaData(required = true, label = "Description", position = 40)
    @Column(name = "description", length = 127, nullable = false)
    private @Getter @Setter String description;

    // 操作者ID
    @FieldMetaData(required = true, label = "Operator ID", position = 50)
    @Column(name = "operId", nullable = false)
    private @Getter @Setter Integer operId;

    // 操作时间
    @FieldMetaData(required = true, label = "Operate Time", dataType = FieldMetaDataSupportedDataType.DATE,
            formatter = FieldMetaDataSupportedDataType.TIMEFORMAT, position = 60)
    @Column(name = "operTime", nullable = false)
    private @Getter @Setter Date operTime;
}
