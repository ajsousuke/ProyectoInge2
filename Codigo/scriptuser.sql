SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP DATABASE IF EXISTS `Edecisiones_BD`;

CREATE SCHEMA IF NOT EXISTS `Edecisiones_BD` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `Edecisiones_BD` ;

-- -----------------------------------------------------
-- Table `Edecisiones_BD`.`Persona`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Edecisiones_BD`.`Persona` (
  `Cedula` INT NOT NULL ,
  `Nombre` VARCHAR(45) NULL ,
  `Apellido1` VARCHAR(45) NULL ,
  `Apellido2` VARCHAR(45) NULL ,
  `Sexo` TINYINT(1) NULL ,
  `FechaNac` DATE NULL ,
  PRIMARY KEY (`Cedula`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Edecisiones_BD`.`Plebiscito`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Edecisiones_BD`.`Plebiscito` (
  `Id_Plebiscito` INT NOT NULL AUTO_INCREMENT ,
  `Nombre_Plebiscito` VARCHAR(255) NULL ,
  `Tipo` TINYINT(1) NULL ,
  `Acceso` TINYINT(1) NULL ,
  `Descripcion` TEXT NULL ,
  `Votacion_Inicio` DATE NULL ,
  `Votacion_Final` DATE NULL ,
  `Insc_Tend_Inicio` DATE NULL ,
  `Insc_Tend_Final` DATE NULL ,
  `Discusion_Inicio` DATE NULL ,
  `Discusion_Final` DATE NULL ,
  `Resultados` DATE NULL ,
  `Usuario_Creador` INT NOT NULL ,
  PRIMARY KEY (`Id_Plebiscito`) ,
  UNIQUE(`Nombre_Plebiscito`) ,
  CONSTRAINT `fk_Plebiscito_Usuario1`
    FOREIGN KEY (`Usuario_Creador` )
    REFERENCES `Edecisiones_BD`.`Usuario` (`Id_Usuario` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Edecisiones_BD`.`Tendencia`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Edecisiones_BD`.`Tendencia` (
  `Id_Tendencia` INT NOT NULL AUTO_INCREMENT ,
  `Nombre_Tendencia` VARCHAR(45) NOT NULL ,
  `Fotografia` MEDIUMBLOB NULL ,
  `Nombre_Fotografia` VARCHAR(255) NULL ,
  `Descripcion` TEXT NOT NULL ,
  `Info_contacto` TEXT NOT NULL ,
  `Plebiscito_Id_Plebiscito` INT NOT NULL ,
  PRIMARY KEY (`Id_Tendencia`) ,
  UNIQUE(`Nombre_Tendencia`) ,
  INDEX `fk_Tendencia_Plebiscito1_idx` (`Plebiscito_Id_Plebiscito` ASC) ,
  CONSTRAINT `fk_Tendencia_Plebiscito1`
    FOREIGN KEY (`Plebiscito_Id_Plebiscito` )
    REFERENCES `Edecisiones_BD`.`Plebiscito` (`Id_Plebiscito` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Edecisiones_BD`.`Usuario`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Edecisiones_BD`.`Usuario` (
  `Id_Usuario` INT NOT NULL AUTO_INCREMENT ,
  `Email` VARCHAR(45) NULL ,
  `Password` VARCHAR(45) NULL ,
  `Fotografia` BLOB NULL ,
  `Persona_Cedula` INT NOT NULL ,
  PRIMARY KEY (`Id_Usuario`, `Persona_Cedula`) ,
  UNIQUE (`Email`) ,
  INDEX `fk_Usuario_Persona1_idx` (`Persona_Cedula` ASC) ,
  CONSTRAINT `fk_Usuario_Persona1`
    FOREIGN KEY (`Persona_Cedula` )
    REFERENCES `Edecisiones_BD`.`Persona` (`Cedula` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Edecisiones_BD`.`RespuestaForo`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Edecisiones_BD`.`RespuestaForo` (
  `Id_RespuestaForo` INT NOT NULL AUTO_INCREMENT ,
  `Respuesta` TEXT NULL ,
  `Numero` INT NULL ,
  `Fecha` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  `Plebiscito_Id_Plebiscito` INT NOT NULL ,
  `Usuario_Id_Usuario` INT NOT NULL ,
  PRIMARY KEY (`Id_RespuestaForo`, `Plebiscito_Id_Plebiscito`) ,
  INDEX `fk_RespuestaForo_Plebiscito1_idx` (`Plebiscito_Id_Plebiscito` ASC) ,
  CONSTRAINT `fk_RespuestaForo_Plebiscito1`
    FOREIGN KEY (`Plebiscito_Id_Plebiscito` )
    REFERENCES `Edecisiones_BD`.`Plebiscito` (`Id_Plebiscito` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_RespuestaForo_Usuario1`
    FOREIGN KEY (`Usuario_Id_Usuario` )
    REFERENCES `Edecisiones_BD`.`Usuario` (`Id_Usuario` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Edecisiones_BD`.`Padron`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Edecisiones_BD`.`Padron` (
  `Persona_Cedula` INT NOT NULL ,
  `Plebiscito_Id_Plebiscito` INT NOT NULL ,
  PRIMARY KEY (`Persona_Cedula`, `Plebiscito_Id_Plebiscito`) ,
  INDEX `fk_Persona_has_Plebiscito_Plebiscito1_idx` (`Plebiscito_Id_Plebiscito` ASC) ,
  INDEX `fk_Persona_has_Plebiscito_Persona_idx` (`Persona_Cedula` ASC) ,
  CONSTRAINT `fk_Persona_has_Plebiscito_Plebiscito1`
    FOREIGN KEY (`Plebiscito_Id_Plebiscito` )
    REFERENCES `Edecisiones_BD`.`Plebiscito` (`Id_Plebiscito` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Edecisiones_BD`.`Voto`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Edecisiones_BD`.`Voto` (
  `Persona_Cedula` INT NOT NULL ,
  `Tendencia_Id_Tendencia` INT NOT NULL ,
  `Hora` DATE NULL ,
  PRIMARY KEY (`Persona_Cedula`, `Tendencia_Id_Tendencia`) ,
  INDEX `fk_Persona_has_Tendencia_Tendencia1_idx` (`Tendencia_Id_Tendencia` ASC) ,
  INDEX `fk_Persona_has_Tendencia_Persona1_idx` (`Persona_Cedula` ASC) ,
  CONSTRAINT `fk_Persona_has_Tendencia_Persona1`
    FOREIGN KEY (`Persona_Cedula` )
    REFERENCES `Edecisiones_BD`.`Persona` (`Cedula` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Persona_has_Tendencia_Tendencia1`
    FOREIGN KEY (`Tendencia_Id_Tendencia` )
    REFERENCES `Edecisiones_BD`.`Tendencia` (`Id_Tendencia` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `Edecisiones_BD`.`LinksExternos`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Edecisiones_BD`.`LinksExternos` (
  `Tendencia_Id_Tendencia` INT NOT NULL,
  `LinkExterno` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`Tendencia_Id_Tendencia`, `LinkExterno`) ,
  CONSTRAINT `fk_linkexterno_tendencia`
    FOREIGN KEY (`Tendencia_Id_Tendencia` )
    REFERENCES `Edecisiones_BD`.`Tendencia` (`Id_Tendencia` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
