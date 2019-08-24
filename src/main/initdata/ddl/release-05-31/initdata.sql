DELETE FROM fieldspec WHERE classFullName LIKE '%FirstlineHaulManifestItem%' ORDER BY id DESC;
DELETE FROM fieldspec WHERE name = 'firstlineHaulManifest';
DELETE FROM fieldspec WHERE name = 'firstlineHaulManifestItems';
DELETE FROM fieldspec WHERE name = 'userDeliveryConfiguration';
DELETE FROM fieldspec WHERE name = 'flightInfo';

UPDATE `ecomerp`.`functionality` SET `positionSn` = '10' WHERE (`code` = 'menu.self');
UPDATE `ecomerp`.`functionality` SET `positionSn` = '20' WHERE (`code` = 'menu.order');
UPDATE `ecomerp`.`functionality` SET `positionSn` = '30' WHERE (`code` = 'menu.warehouse');
UPDATE `ecomerp`.`functionality` SET `positionSn` = '40' WHERE (`code` = 'menu.firstLineHaul');
UPDATE `ecomerp`.`functionality` SET `positionSn` = '50' WHERE (`code` = 'menu.thirdpart');
UPDATE `ecomerp`.`functionality` SET `positionSn` = '60' WHERE (`code` = 'menu.billing');
UPDATE `ecomerp`.`functionality` SET `positionSn` = '70' WHERE (`code` = 'menu.workflow');
UPDATE `ecomerp`.`functionality` SET `positionSn` = '80' WHERE (`code` = 'menu.system');
