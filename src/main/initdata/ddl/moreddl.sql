ALTER TABLE `ecomerp`.`useraccountrole_functionality` 
DROP FOREIGN KEY `FK_useraccountrole_Functionality_functionalty_Id`;
ALTER TABLE `ecomerp`.`useraccountrole_functionality` 
ADD CONSTRAINT `FK_useraccountrole_Functionality_functionalty_Id`
  FOREIGN KEY (`functionalityId`)
  REFERENCES `ecomerp`.`functionality` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
