CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`admin` (
  `admin_num` INT NOT NULL AUTO_INCREMENT,
  `admin_id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `phone_number` CHAR(11) NOT NULL,
  PRIMARY KEY (`admin_num`),
  UNIQUE INDEX `admin_id_UNIQUE` (`admin_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


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
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `mydb`.`machine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`machine` (
  `machine_id` INT NOT NULL,
  `machine_detail` VARCHAR(255) NOT NULL,
  `broken` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`machine_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `mydb`.`log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`log` (
  `log_num` INT NOT NULL AUTO_INCREMENT,
  `error_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `error_message` VARCHAR(255) NULL DEFAULT NULL,
  `machine_id` INT NOT NULL,
  PRIMARY KEY (`log_num`),
  INDEX `machine_key_idx` (`machine_id` ASC),
  CONSTRAINT `machine_key`
    FOREIGN KEY (`machine_id`)
    REFERENCES `mydb`.`machine` (`machine_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `mydb`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`product` (
  `product_num` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `stock` INT NOT NULL DEFAULT '1',
  `detail` VARCHAR(100) NULL DEFAULT NULL,
  `price` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_num`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `mydb`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`orders` (
  `order_num` INT UNSIGNED NOT NULL,
  `order_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_num` INT NOT NULL,
  `detail_address` VARCHAR(100),
  `address` INT NOT NULL,
  PRIMARY KEY (`order_num`),
  INDEX `customer_key_idx` (`customer_num` ASC),
  CONSTRAINT `customer_key`
    FOREIGN KEY (`customer_num`)
    REFERENCES `mydb`.`customer` (`customer_num`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `mydb`.`orderdetail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`orderdetail` (
  `order_detail_id` INT NOT NULL AUTO_INCREMENT,
  `amount` INT NOT NULL DEFAULT '1',
  `product_num` INT NOT NULL,
  `order_num` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`order_detail_id`, `order_num`),
  INDEX `product_key_idx` (`product_num` ASC),
  INDEX `fk_orderdetail_orders1_idx` (`order_num` ASC),
  CONSTRAINT `product_key`
    FOREIGN KEY (`product_num`)
    REFERENCES `mydb`.`product` (`product_num`),
  CONSTRAINT `fk_orderdetail_orders1`
    FOREIGN KEY (`order_num`)
    REFERENCES `mydb`.`orders` (`order_num`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `mydb`.`ordernow`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ordernow` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `order_num` INT UNSIGNED NOT NULL,
  `status` TINYINT NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_Orders_TO_OrderNow_1`
    FOREIGN KEY (`order_num`)
    REFERENCES `mydb`.`orders` (`order_num`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

insert into customer values (1,"ssafy","ssafy","왜왜왜","01012345678");

insert into admin values(1,"admin","admin","01071328770");

insert into orders values
(2307000001,current_timestamp(),1,"A204",1)
,(2307000002,current_timestamp(),1,"801",2)
,(2307000003,current_timestamp(),1,"802",3);

insert into ordernow values
(1,2307000001,1),
(2,2307000002,2),
(3,2307000003,1);

insert into product values
(1,"휴지",1000,"4겹 휴지",10000),
(2,"소고기",200,"1등급 한우",1000000),
(3,"USB",333,"256GB USB",80000);

insert into orderdetail values
(1,3,1,2307000001),
(2,1,2,2307000001),
(3,1,3,2307000001),
(4,1,1,2307000002),
(5,2,2,2307000002),
(6,10,1,2307000003),
(7,5,3,2307000003);

insert into machine values
(0,"분류용",false),
(1001,"피스톤1",true),
(1002,"피스톤2",true),
(1003,"피스톤3",false);

insert into log values
(1,current_timestamp(),"test고장1",1001),
(2,current_timestamp(),"test고장22",1002),
(3,current_timestamp(),"test고장333",1002);
