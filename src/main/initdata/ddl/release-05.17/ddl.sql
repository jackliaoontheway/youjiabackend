CREATE TABLE `ecomerp`.`filterStrategy`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdBy` int(11) NOT NULL,
  `createdDate` datetime(0) NOT NULL,
  `strategyName` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `classFullName` varchar(220) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;


CREATE TABLE `ecomerp`.`filterStrategyItem`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdBy` int(11) NOT NULL,
  `createdDate` datetime(0) NOT NULL,
  `fieldFullName` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `fieldName` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `filterOperator` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `filterValue` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `message` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `rank` int(11) NULL DEFAULT NULL,
  `filterStrategyId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_padr6oiwjt4diqbwq400vdudc`(`fieldFullName`) USING BTREE,
  INDEX `FK_ModelFilter_Strategy_Item_Id`(`filterStrategyId`) USING BTREE,
  CONSTRAINT `FK_ModelFilter_Strategy_Item_Id` FOREIGN KEY (`filterStrategyId`) REFERENCES `ecomerp`.`filterStrategy` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;


CREATE TABLE `ecomerp`.`flightinfo`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdBy` int(11) NOT NULL,
  `createdDate` datetime(0) NOT NULL,
  `arrivingTime` datetime(0) NULL DEFAULT NULL,
  `departTime` datetime(0) NULL DEFAULT NULL,
  `destination` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `flightNum` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `origin` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `ecomerp`.`firstlinehaulmanifest`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdBy` int(11) NOT NULL,
  `createdDate` datetime(0) NOT NULL,
  `wfPid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `workflowStep` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `manifestStatus` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mawb` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `flightInfoId` int(11) NULL DEFAULT NULL,
  `ownerId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_firstlinehaulmanifest_flightinfo_flightinfoid`(`flightInfoId`) USING BTREE,
  INDEX `fk_firstlinehaulmanifest_useraccount_ownerid`(`ownerId`) USING BTREE,
  CONSTRAINT `fk_firstlinehaulmanifest_flightinfo_flightinfoid` FOREIGN KEY (`flightInfoId`) REFERENCES `ecomerp`.`flightinfo` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_firstlinehaulmanifest_useraccount_ownerid` FOREIGN KEY (`ownerId`) REFERENCES `ecomerp`.`useraccount` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;




CREATE TABLE `ecomerp`.`firstlinehaulmanifestitem`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdBy` int(11) NOT NULL,
  `createdDate` datetime(0) NOT NULL,
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contents` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `declaredValue` double NULL DEFAULT NULL,
  `declaredCurrency` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deliveryOrderNum` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hawb` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `numOfPieces` int(11) NULL DEFAULT NULL,
  `packageWeightUnit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `packageWeight` double NULL DEFAULT NULL,
  `firstlineHaulManifestId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_FirstlineHaulManifestItem_Item_Id`(`firstlineHaulManifestId`) USING BTREE,
  CONSTRAINT `FK_FirstlineHaulManifestItem_Item_Id` FOREIGN KEY (`firstlineHaulManifestId`) REFERENCES `ecomerp`.`firstlinehaulmanifest` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;





ALTER TABLE `ecomerp`.`deliveryorder` ADD COLUMN `firstlinehaulManifestId` int(11) NULL DEFAULT NULL AFTER `wfPid`;

ALTER TABLE `ecomerp`.`deliveryorderitem` ADD COLUMN `checkedWeight` double NULL DEFAULT NULL AFTER `ancillaryEndorsementType`;

ALTER TABLE `ecomerp`.`deliveryorder` ADD INDEX `fk_deliveryorder_firstlinehaulmanifest_id`(`firstlinehaulManifestId`) USING BTREE;

ALTER TABLE `ecomerp`.`deliveryorder` ADD CONSTRAINT `fk_deliveryorder_firstlinehaulmanifest_id` FOREIGN KEY (`firstlinehaulManifestId`) REFERENCES `ecomerp`.`firstlinehaulmanifest` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
