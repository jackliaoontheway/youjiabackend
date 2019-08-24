ALTER TABLE `ecomerp`.`tradingaccountconfig`
ADD COLUMN `fileName` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL;
ALTER TABLE `ecomerp`.`tradingaccountconfig`
ADD COLUMN `fileType` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL;
