INSERT INTO `ecomerp`.`expresscorp` 
(`createdBy`, `createdDate`, `jsonFields`, `addressValidationSupported`, `code`, `corpConfigClassName`, `fakeSenderAddressSupported`, 
`homeDeliverySupported`, `manifestNeeded`, `multiParcelSupported`, `officialTrackingUrl`, `serviceBaseUrl`, `serviceImplClassName`,
 `signatureSupported`, `virtualCorp`) 
VALUES ('-1', now(), NULL, 0, 'PostPony', 'com.ecomerp.express.api.postpony.PostPonyConfiguration', 1, 1, 1, 0, 'https://api.postpony.com/api/', 
'https://api.postpony.com/api/', 'com.ecomerp.express.api.postpony.PostPonyAPI', 1, 0);


INSERT INTO `ecomerp`.`expresscorp` 
(`createdBy`, `createdDate`, `jsonFields`, `addressValidationSupported`, `code`, `corpConfigClassName`, `fakeSenderAddressSupported`, 
`homeDeliverySupported`, `manifestNeeded`, `multiParcelSupported`, `officialTrackingUrl`, `serviceBaseUrl`, `serviceImplClassName`,
 `signatureSupported`, `virtualCorp`) 
VALUES ('-1', now(), NULL, 0, 'DragonParcel', 'com.ecomerp.express.api.dragonparcel.DragonParcelConfiguration', 1, 1, 1, 0, 'http://www.dragonparcel.com', 
'http://www.dragonparcel.com', 'com.ecomerp.express.api.dragonparcel.DragonParcelAPI', 1, 1);

INSERT INTO `ecomerp`.`expressaccount` (`createdBy`, `createdDate`, `jsonFields`, `accountConfigClassName`, `code`, `languageOnLabel`, `shared`, `expressCorpId`, `ownerId`) 
VALUES ('-1', now(), NULL, 'com.ecomerp.express.api.dragonparcel.DragonParcelAccountConfiguration', 'DragonParcelAccount', 'en-us', 1, 
(SELECT id FROM expresscorp WHERE code = 'DragonParcel'), '5');

INSERT INTO `ecomerp`.`expressaccount` (`createdBy`, `createdDate`, `jsonFields`, `accountConfigClassName`, `code`, `languageOnLabel`, `shared`, `expressCorpId`, `ownerId`) 
VALUES ('-1', now(), NULL, 'com.ecomerp.express.api.postpony.PostPonyAccountConfiguration', 'PostPonyAccount', 'en-us', 1, 
(SELECT id FROM expresscorp WHERE code = 'PostPony'), '5');

INSERT INTO `ecomerp`.`servicetype` (`classification`, `createdBy`, `createdDate`, `basisService`, `maxDuration`, `minDuration`, 
`serviceCode`, `serviceTypeCode`, `expressAccountConfigId`, `expressCorpId`) 
VALUES ('ExpressServiceProduct', '-1', now(), 0, 8, 3, 'USPS-FirstClassMail', 'DragonParcel-USPS-FirstClassMail', 
(SELECT id FROM expressaccount WHERE code = 'DragonParcelAccount'), 
(SELECT id FROM expresscorp WHERE code = 'DragonParcel'));

INSERT INTO `ecomerp`.`servicetype` (`classification`, `createdBy`, `createdDate`, `basisService`, `maxDuration`, `minDuration`, 
`serviceCode`, `serviceTypeCode`, `expressAccountConfigId`, `expressCorpId`) 
VALUES ('ExpressServiceProduct', '-1', now(), 0, 8, 3, 'USPS-PriorityMail', 'DragonParcel-USPS-PriorityMail', 
(SELECT id FROM expressaccount WHERE code = 'DragonParcelAccount'), 
(SELECT id FROM expresscorp WHERE code = 'DragonParcel'));

INSERT INTO `ecomerp`.`servicetype` (`classification`, `createdBy`, `createdDate`, `basisService`, `maxDuration`, `minDuration`, 
`serviceCode`, `serviceTypeCode`, `expressAccountConfigId`, `expressCorpId`) 
VALUES ('ExpressCorpServiceType', '-1', now(), 1, 8, 3, 'UspsFirstClassMail', 'PostPony-UspsFirstClassMail', 
(SELECT id FROM expressaccount WHERE code = 'PostPonyAccount'), 
(SELECT id FROM expresscorp WHERE code = 'PostPony'));

INSERT INTO `ecomerp`.`servicetype` (`classification`, `createdBy`, `createdDate`, `basisService`, `maxDuration`, `minDuration`, 
`serviceCode`, `serviceTypeCode`, `expressAccountConfigId`, `expressCorpId`) 
VALUES ('ExpressCorpServiceType', '-1', now(), 1, 8, 3, 'UspsPriorityMail', 'PostPony-UspsPriorityMail', 
(SELECT id FROM expressaccount WHERE code = 'PostPonyAccount'), 
(SELECT id FROM expresscorp WHERE code = 'PostPony'));

INSERT INTO expressaccount_servicetype (expressAccountId,serviceTypeId) VALUES (
(SELECT id FROM expressaccount WHERE code = 'DragonParcelAccount'),
(SELECT id FROM servicetype WHERE serviceTypeCode = 'DragonParcel-USPS-PriorityMail')
);
INSERT INTO expressaccount_servicetype (expressAccountId,serviceTypeId) VALUES (
(SELECT id FROM expressaccount WHERE code = 'DragonParcelAccount'),
(SELECT id FROM servicetype WHERE serviceTypeCode = 'DragonParcel-USPS-FirstClassMail')
);

INSERT INTO expressaccount_servicetype (expressAccountId,serviceTypeId) VALUES (
(SELECT id FROM expressaccount WHERE code = 'PostPonyAccount'),
(SELECT id FROM servicetype WHERE serviceTypeCode = 'PostPony-UspsFirstClassMail')
);
INSERT INTO expressaccount_servicetype (expressAccountId,serviceTypeId) VALUES (
(SELECT id FROM expressaccount WHERE code = 'PostPonyAccount'),
(SELECT id FROM servicetype WHERE serviceTypeCode = 'PostPony-UspsPriorityMail')
);

INSERT INTO `ecomerp`.`servicefulfilment` (`createdBy`, `createdDate`, `serviceTypeId`) VALUES ('-1',now(), (SELECT id FROM servicetype WHERE serviceTypeCode = 'DragonParcel-USPS-FirstClassMail'));
INSERT INTO `ecomerp`.`servicepart` (`createdBy`, `createdDate`, `partSn`, `expressAccountId`, `serviceTypeId`) VALUES ('-1', now(), 3, (SELECT id FROM expressaccount WHERE code = 'PostPonyAccount'), (SELECT id FROM servicetype WHERE serviceTypeCode = 'PostPony-UspsFirstClassMail'));

INSERT INTO servicefulfilment_servicepart (`servicefulfilmentId`,`servicepartId`) values (
(SELECT id FROM servicefulfilment WHERE serviceTypeId = (SELECT id FROM servicetype WHERE serviceTypeCode = 'DragonParcel-USPS-FirstClassMail')),
(SELECT id FROM servicepart WHERE serviceTypeId = (SELECT id FROM servicetype WHERE serviceTypeCode = 'PostPony-UspsFirstClassMail'))
);
INSERT INTO `ecomerp`.`servicefulfilment` (`createdBy`, `createdDate`, `serviceTypeId`) VALUES ('-1', now(), (SELECT id FROM servicetype WHERE serviceTypeCode = 'DragonParcel-USPS-PriorityMail'));
INSERT INTO `ecomerp`.`servicepart` (`createdBy`, `createdDate`, `partSn`, `expressAccountId`, `serviceTypeId`) VALUES ('-1', now(), 3, (SELECT id FROM expressaccount WHERE code = 'PostPonyAccount'), (SELECT id FROM servicetype WHERE serviceTypeCode = 'PostPony-UspsPriorityMail'));
INSERT INTO servicefulfilment_servicepart (`servicefulfilmentId`,`servicepartId`) values (
(SELECT id FROM servicefulfilment WHERE serviceTypeId = (SELECT id FROM servicetype WHERE serviceTypeCode = 'DragonParcel-USPS-PriorityMail')),
(SELECT id FROM servicepart WHERE serviceTypeId = (SELECT id FROM servicetype WHERE serviceTypeCode = 'PostPony-UspsPriorityMail'))
);


UPDATE `ecomerp`.`expresscorp` SET `jsonFields` = '{\"headerKeyName\":\"Content-Type\",\"headerKeyValue\":\"application/xml\"}' WHERE (code = 'PostPony');
UPDATE `ecomerp`.`expressaccount` SET `jsonFields` = '{\"key\":\"416b41ab\",\"password\":\"ebce275d883d47\",\"authorizedKey\":\"7ZmPBvNDuWL0uxDEkpuA\"}' WHERE (code = 'PostPonyAccount');
