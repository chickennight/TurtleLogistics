CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb4;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `admin` (
  `admin_num` INT NOT NULL AUTO_INCREMENT,
  `admin_id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `phone_number` CHAR(11) NOT NULL,
  PRIMARY KEY (`admin_num`),
  UNIQUE INDEX `admin_id_UNIQUE` (`admin_id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `customer` (
  `customer_num` INT NOT NULL AUTO_INCREMENT,
  `customer_id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `phone_number` CHAR(11) NOT NULL,
  PRIMARY KEY (`customer_num`),
  UNIQUE INDEX `customer_id_UNIQUE` (`customer_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `machine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `machine` (
  `machine_id` INT NOT NULL,
  `machine_detail` VARCHAR(255) NOT NULL,
  `broken` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`machine_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `log` (
  `log_num` INT NOT NULL AUTO_INCREMENT,
  `error_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `error_message` VARCHAR(255) NULL DEFAULT NULL,
  `machine_id` INT NOT NULL,
  PRIMARY KEY (`log_num`),
  INDEX `machine_key_idx` (`machine_id` ASC),
  CONSTRAINT `machine_key`
    FOREIGN KEY (`machine_id`)
    REFERENCES `machine` (`machine_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `orders` (
  `order_num` INT UNSIGNED NOT NULL,
  `order_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_num` INT NOT NULL,
  `detail_address` VARCHAR(100) NULL DEFAULT NULL,
  `address` INT NOT NULL,
  PRIMARY KEY (`order_num`),
  INDEX `customer_key_idx` (`customer_num` ASC),
  CONSTRAINT `customer_key`
    FOREIGN KEY (`customer_num`)
    REFERENCES `customer` (`customer_num`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `product` (
  `product_num` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `stock` INT NOT NULL DEFAULT '1',
  `detail` VARCHAR(100) NULL DEFAULT NULL,
  `price` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_num`))
ENGINE = InnoDB
AUTO_INCREMENT = 21
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `orderdetail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `orderdetail` (
  `order_detail_id` INT NOT NULL AUTO_INCREMENT,
  `amount` INT NOT NULL DEFAULT '1',
  `product_num` INT NOT NULL,
  `order_num` INT UNSIGNED NOT NULL,
  `order_date` INT NOT NULL,
  PRIMARY KEY (`order_detail_id`),
  INDEX `product_key_idx` (`product_num` ASC),
  INDEX `fk_orderdetail_orders_idx` (`order_num` ASC),
  CONSTRAINT `fk_orderdetail_orders`
    FOREIGN KEY (`order_num`)
    REFERENCES `orders` (`order_num`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `product_key`
    FOREIGN KEY (`product_num`)
    REFERENCES `product` (`product_num`))
ENGINE = InnoDB
AUTO_INCREMENT = 7018525
DEFAULT CHARACTER SET = utf8mb4;


CREATE TABLE IF NOT EXISTS `ordernow` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `order_num` INT UNSIGNED NOT NULL,
  `status` TINYINT NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  INDEX `FK_Orders_TO_OrderNow` (`order_num` ASC),
  CONSTRAINT `FK_Orders_TO_OrderNow`
    FOREIGN KEY (`order_num`)
    REFERENCES `orders` (`order_num`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `mydb`.`image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `image` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `log_num` INT NOT NULL,
  `content_type` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `log_num_foreignkey_idx` (`log_num` ASC),
  CONSTRAINT `log_num_foreignkey`
    FOREIGN KEY (`log_num`)
    REFERENCES `log` (`log_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE INDEX idx_orderdetail_product_date_amount ON orderdetail(product_num, order_date, amount);

