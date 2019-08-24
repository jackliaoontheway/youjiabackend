ALTER TABLE `ecomerp`.`airfreightbox` ADD COLUMN `preIndicate` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `boxMark`;

ALTER TABLE `ecomerp`.`airfreightbox` ADD COLUMN `sn` int(11) NULL DEFAULT NULL AFTER `preIndicate`;

ALTER TABLE `ecomerp`.`airfreightbox` ADD COLUMN `firstlineHaulManifestId` int(11) NULL DEFAULT NULL AFTER `sn`;

ALTER TABLE `ecomerp`.`airfreightbox` ADD CONSTRAINT `fk_firstlinehaulmanifest_airfreightbox_id` FOREIGN KEY (`firstlineHaulManifestId`) REFERENCES `ecomerp`.`firstlinehaulmanifest` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `ecomerp`.`airfreightbox` ADD INDEX `fk_firstlinehaulmanifest_airfreightbox_id`(`firstlineHaulManifestId`) USING BTREE;

ALTER TABLE `ecomerp`.`deliveryorder` DROP FOREIGN KEY `fk_deliveryorder_firstlinehaulmanifest_id`;

ALTER TABLE `ecomerp`.`deliveryorder` DROP INDEX `fk_deliveryorder_firstlinehaulmanifest_id`;

ALTER TABLE `ecomerp`.`deliveryorder` ADD COLUMN `airfreightboxId` int(11) NULL DEFAULT NULL AFTER `wfPid`;

ALTER TABLE `ecomerp`.`deliveryorder` DROP COLUMN `firstlinehaulManifestId`;

ALTER TABLE `ecomerp`.`deliveryorder` ADD CONSTRAINT `fk_DeliveryOrder_AirFreightBox_id` FOREIGN KEY (`airfreightboxId`) REFERENCES `ecomerp`.`airfreightbox` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `ecomerp`.`deliveryorder` ADD INDEX `fk_DeliveryOrder_AirFreightBox_id`(`airfreightboxId`) USING BTREE;

ALTER TABLE `ecomerp`.`firstlinehaulmanifest` DROP FOREIGN KEY `fk_firstlinehaulmanifest_flightinfo_flightinfoid`;

ALTER TABLE `ecomerp`.`firstlinehaulmanifest` DROP INDEX `fk_firstlinehaulmanifest_flightinfo_flightinfoid`;

ALTER TABLE `ecomerp`.`firstlinehaulmanifest` DROP COLUMN `flightInfoId`;

ALTER TABLE `ecomerp`.`flightinfo` ADD COLUMN `firstlineHaulManifestId` int(11) NULL DEFAULT NULL AFTER `origin`;

ALTER TABLE `ecomerp`.`flightinfo` ADD CONSTRAINT `fk_firstlinehaulmanifest_flightinfo_id` FOREIGN KEY (`firstlineHaulManifestId`) REFERENCES `ecomerp`.`firstlinehaulmanifest` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `ecomerp`.`flightinfo` ADD INDEX `fk_firstlinehaulmanifest_flightinfo_id`(`firstlineHaulManifestId`) USING BTREE;

DROP TABLE `ecomerp`.`airfreightbox_deliveryorder`;

DROP TABLE `ecomerp`.`firstlinehaulmanifestitem`;