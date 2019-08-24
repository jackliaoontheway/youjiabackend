
CREATE TABLE `ecomerp`.`useraccountgroup`  (
  `classification` varchar(31) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdBy` int(11) NOT NULL,
  `createdDate` datetime(0) NOT NULL,
  `code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `label` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `positionSn` int(11) NOT NULL,
  `groupManagerId` int(11) NULL DEFAULT NULL,
  `ownerId` int(11) NULL DEFAULT NULL,
  `parentId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_useraccountgroup_ownerId_code`(`classification`, `ownerId`, `code`) USING BTREE,
  INDEX `fk_useraccountgroup_manager_id`(`groupManagerId`) USING BTREE,
  INDEX `fk_useraccountgroup_useraccount_ownerid`(`ownerId`) USING BTREE,
  INDEX `fk_subdivision_division_id`(`parentId`) USING BTREE,
  CONSTRAINT `fk_subdivision_division_id` FOREIGN KEY (`parentId`) REFERENCES `ecomerp`.`useraccountgroup` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_useraccountgroup_manager_id` FOREIGN KEY (`groupManagerId`) REFERENCES `ecomerp`.`useraccount` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_useraccountgroup_useraccount_ownerid` FOREIGN KEY (`ownerId`) REFERENCES `ecomerp`.`useraccount` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `ecomerp`.`useraccountgroup_role`  (
  `usergroupId` int(11) NOT NULL,
  `useraccountroleId` int(11) NOT NULL,
  INDEX `fk_useraccountgroup_role_role_id`(`useraccountroleId`) USING BTREE,
  INDEX `fk_useraccountgroup_role_useraccountgroup_id`(`usergroupId`) USING BTREE,
  CONSTRAINT `fk_useraccountgroup_role_role_id` FOREIGN KEY (`useraccountroleId`) REFERENCES `ecomerp`.`useraccountrole` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_useraccountgroup_role_useraccountgroup_id` FOREIGN KEY (`usergroupId`) REFERENCES `ecomerp`.`useraccountgroup` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `ecomerp`.`workgroup_useraccount`  (
  `userAccountId` int(11) NOT NULL,
  `workgroupId` int(11) NOT NULL,
  INDEX `fk_useraccount_workgroup_workgroup_id`(`workgroupId`) USING BTREE,
  INDEX `fk_useraccount_workgroup_useraccount_id`(`userAccountId`) USING BTREE,
  CONSTRAINT `fk_useraccount_workgroup_useraccount_id` FOREIGN KEY (`userAccountId`) REFERENCES `ecomerp`.`useraccountgroup` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_useraccount_workgroup_workgroup_id` FOREIGN KEY (`workgroupId`) REFERENCES `ecomerp`.`useraccount` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE `ecomerp`.`airfreightbox` ADD COLUMN `ownerId` int(11) NULL DEFAULT NULL AFTER `firstlineHaulManifestId`;

ALTER TABLE `ecomerp`.`airfreightbox` ADD CONSTRAINT `fk_airfreightbox_useraccount_ownerid` FOREIGN KEY (`ownerId`) REFERENCES `ecomerp`.`useraccount` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `ecomerp`.`airfreightbox` ADD INDEX `fk_airfreightbox_useraccount_ownerid`(`ownerId`) USING BTREE;

ALTER TABLE `ecomerp`.`fieldspec` ADD COLUMN `detailHide` bit(1) NULL DEFAULT NULL AFTER `tip`;

ALTER TABLE `ecomerp`.`useraccount` ADD COLUMN `divisionGroupId` int(11) NULL DEFAULT NULL AFTER `userDeliveryConfigurationId`;

ALTER TABLE `ecomerp`.`useraccount` ADD COLUMN `ownerId` int(11) NULL DEFAULT NULL AFTER `divisionGroupId`;

ALTER TABLE `ecomerp`.`useraccount` ADD CONSTRAINT `fk_useraccount_division_group_id` FOREIGN KEY (`divisionGroupId`) REFERENCES `ecomerp`.`useraccountgroup` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `ecomerp`.`useraccount` ADD CONSTRAINT `fk_useraccount_useraccount_ownerid` FOREIGN KEY (`ownerId`) REFERENCES `ecomerp`.`useraccount` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `ecomerp`.`useraccount` ADD INDEX `fk_useraccount_division_group_id`(`divisionGroupId`) USING BTREE;

ALTER TABLE `ecomerp`.`useraccount` ADD INDEX `fk_useraccount_useraccount_ownerid`(`ownerId`) USING BTREE;

ALTER TABLE `ecomerp`.`useraccountrole` ADD COLUMN `ownerId` int(11) NULL DEFAULT NULL AFTER `userAccountConfigId`;

ALTER TABLE `ecomerp`.`useraccountrole` ADD CONSTRAINT `fk_useraccountrole_useraccount_ownerid` FOREIGN KEY (`ownerId`) REFERENCES `ecomerp`.`useraccount` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `ecomerp`.`useraccountrole` ADD UNIQUE INDEX `UK_UserAccountRole_Code_OwnerId`(`code`, `ownerId`) USING BTREE;

ALTER TABLE `ecomerp`.`useraccountrole` ADD INDEX `fk_useraccountrole_useraccount_ownerid`(`ownerId`) USING BTREE;

ALTER TABLE `ecomerp`.`usersetting` ADD COLUMN `tableHiddenFields` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `workLang`;


