CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `mydb` ;

CREATE TABLE IF NOT EXISTS `mydb`.`admin` (
  `admin_id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `phone_number` CHAR(11) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE INDEX `admin_id_UNIQUE` (`admin_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`customer` (
  `customer_num` INT NOT NULL AUTO_INCREMENT,
  `customer_id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `phone_number` CHAR(11) NOT NULL,
  PRIMARY KEY (`customer_num`),
  UNIQUE INDEX `customer_id_UNIQUE` (`customer_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`machine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`machine` (
  `machine_id` INT NOT NULL,
  `machine_detail` VARCHAR(255) NOT NULL,
  `broken` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`machine_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`log` (
  `log_num` INT NOT NULL AUTO_INCREMENT,
  `error_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `error_message` VARCHAR(255) NULL DEFAULT NULL,
  `machine_id` INT NOT NULL,
  PRIMARY KEY (`log_num`),
  INDEX `machine_key_idx` (`machine_id` ASC) ,
  CONSTRAINT `machine_key`
    FOREIGN KEY (`machine_id`)
    REFERENCES `mydb`.`machine` (`machine_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`orders` (
  `order_num` CHAR(12) NOT NULL,
  `order_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_num` INT NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`order_num`),
  INDEX `customer_key_idx` (`customer_num` ASC) ,
  CONSTRAINT `customer_key`
    FOREIGN KEY (`customer_num`)
    REFERENCES `mydb`.`customer` (`customer_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `mydb`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`product` (
  `product_num` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `stock` INT NOT NULL DEFAULT 1,
  `detail` VARCHAR(100) NULL DEFAULT NULL,
  `price` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`product_num`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`orderdetail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`orderdetail` (
  `order_detail_id` INT NOT NULL AUTO_INCREMENT,
  `amount` INT NOT NULL DEFAULT 1,
  `product_num` INT NOT NULL,
  `order_num` CHAR(12) NOT NULL,
  PRIMARY KEY (`order_detail_id`),
  INDEX `order_key_idx` (`order_num` ASC) ,
  INDEX `product_key_idx` (`product_num` ASC) ,
  CONSTRAINT `order_key`
    FOREIGN KEY (`order_num`)
    REFERENCES `mydb`.`orders` (`order_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_key`
    FOREIGN KEY (`product_num`)
    REFERENCES `mydb`.`product` (`product_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`ordernow`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ordernow` (
  `order_num` CHAR(12) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`order_num`),
  CONSTRAINT `FK_Orders_TO_OrderNow_1`
    FOREIGN KEY (`order_num`)
    REFERENCES `mydb`.`orders` (`order_num`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
