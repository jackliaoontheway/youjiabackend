ALTER TABLE `ecomerp`.`flightinfo` 
CHANGE COLUMN `arrivingTime` `arrivingTime` VARCHAR(255) NULL DEFAULT NULL ,
CHANGE COLUMN `departTime` `departTime` VARCHAR(255) NULL DEFAULT NULL ;

UPDATE `ecomerp`.`fieldspec` SET `formatter` = '${weight}${unit}' WHERE name = 'packageWeightInfo';
