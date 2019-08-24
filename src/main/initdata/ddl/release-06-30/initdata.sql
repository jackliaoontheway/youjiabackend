INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.ecomerp.warehouse.model.AirFreightBox.CREATE', 'Create AirFreightBox', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.ecomerp.warehouse.model.AirFreightBox.READ', 'Read AirFreightBox', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.ecomerp.warehouse.model.AirFreightBox.UPDATE', 'Update AirFreightBox', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.ecomerp.warehouse.model.AirFreightBox.DELETE', 'Delete AirFreightBox', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.polarj.model.WorkGroup.CREATE', 'Create WorkGroup', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.polarj.model.WorkGroup.READ', 'Read WorkGroup', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.polarj.model.WorkGroup.UPDATE', 'Update WorkGroup', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.polarj.model.WorkGroup.DELETE', 'Delete WorkGroup', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.polarj.model.DivisionGroup.CREATE', 'Create DivisionGroup', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.polarj.model.DivisionGroup.READ', 'Read DivisionGroup', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.polarj.model.DivisionGroup.UPDATE', 'Update DivisionGroup', '0', 'MODEL_OPERATION');
INSERT INTO `ecomerp`.`functionality` (`createdBy`, `createdDate`, `code`, `label`, `positionSn`, `type`) 
VALUES ('-1', now(), 'com.polarj.model.DivisionGroup.DELETE', 'Delete DivisionGroup', '0', 'MODEL_OPERATION');

UPDATE functionality SET parentId = (SELECT id FROM (SELECT id FROM functionality WHERE code = 'menu.system.modelspec') AS temp) 
WHERE type = 'MODEL_OPERATION';

UPDATE `ecomerp`.`useraccountrole` SET `ownerId` = (SELECT id FROM useraccount WHERE loginName = 'admin@test.com');

UPDATE `ecomerp`.`useraccount` SET `ownerId` = (SELECT id FROM (SELECT id FROM useraccount WHERE loginName = 'admin@test.com') AS temp);

INSERT INTO useraccountrole_functionality (useraccountroleId,functionalityId) values (
(SELECT id FROM useraccountrole WHERE code = 'admin'),
(SELECT id FROM functionality WHERE code = 'com.polarj.model.UserAccountRole.GrantFuncationalityToUserRole')
);