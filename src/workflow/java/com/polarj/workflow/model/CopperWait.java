package com.polarj.workflow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

// 这是引擎中的一个数据库表，在代码中做这个的模型来访问相应数据
/* 用于创建这个表的MySQL DDL语句
CREATE TABLE IF NOT EXISTS `COP_WAIT` (
  `CORRELATION_ID` VARCHAR(128) NOT NULL,
  `WORKFLOW_INSTANCE_ID` VARCHAR(128) NOT NULL,
  `MIN_NUMB_OF_RESP` SMALLINT(6) NOT NULL,
  `TIMEOUT_TS` TIMESTAMP NULL DEFAULT NULL,
  `STATE` TINYINT(4) NOT NULL,
  `PRIORITY` TINYINT(4) NOT NULL,
  `PPOOL_ID` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`CORRELATION_ID`),
  KEY `IDX_COP_WAIT_WFI_ID` (`WORKFLOW_INSTANCE_ID`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;
*/
@Entity
@Table(name = "COP_WAIT")
public class CopperWait
{
    @Id
    @Column(name = "CORRELATION_ID", length = 128, nullable = false)
    private @Getter @Setter String correlationId;

    @Column(name = "WORKFLOW_INSTANCE_ID", length = 128, nullable = false)
    private @Getter @Setter String workflowInstanceId;

    @Column(name = "MIN_NUMB_OF_RESP", nullable = false, columnDefinition = "SMALLINT")
    private @Getter @Setter Integer minNumOfResponses;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMEOUT_TS")
    private @Getter @Setter Date timeoutTs;

    @Column(name = "STATE", nullable = false, columnDefinition = "TINYINT")
    private @Getter @Setter Integer state;

    @Column(name = "PRIORITY", nullable = false, columnDefinition = "TINYINT")
    private @Getter @Setter Integer priority;

    @Column(name = "PPOOL_ID", length = 32, nullable = false)
    private @Getter @Setter String ppoolId;
}
