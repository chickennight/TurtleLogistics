-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: i9a204.p.ssafy.io    Database: mydb
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_num` int NOT NULL AUTO_INCREMENT,
  `admin_id` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone_number` char(11) NOT NULL,
  PRIMARY KEY (`admin_num`),
  UNIQUE KEY `admin_id_UNIQUE` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_num` int NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `phone_number` char(11) NOT NULL,
  PRIMARY KEY (`customer_num`),
  UNIQUE KEY `customer_id_UNIQUE` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content_type` varchar(255) DEFAULT NULL,
  `log_num` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeq8o4q7840s1oo3390meqnobh` (`log_num`),
  CONSTRAINT `FKeq8o4q7840s1oo3390meqnobh` FOREIGN KEY (`log_num`) REFERENCES `log` (`log_num`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `log_num` int NOT NULL AUTO_INCREMENT,
  `error_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `error_message` varchar(255) DEFAULT NULL,
  `machine_id` int NOT NULL,
  `recorded` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`log_num`),
  KEY `machine_key_idx` (`machine_id`),
  CONSTRAINT `machine_key` FOREIGN KEY (`machine_id`) REFERENCES `machine` (`machine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10200027 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `machine`
--

DROP TABLE IF EXISTS `machine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `machine` (
  `machine_id` int NOT NULL,
  `machine_detail` varchar(255) NOT NULL,
  `broken` tinyint DEFAULT '0',
  PRIMARY KEY (`machine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orderdetail`
--

DROP TABLE IF EXISTS `orderdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderdetail` (
  `order_detail_id` int NOT NULL AUTO_INCREMENT,
  `amount` int NOT NULL DEFAULT '1',
  `product_num` int NOT NULL,
  `order_num` int unsigned NOT NULL,
  `order_date` int NOT NULL,
  PRIMARY KEY (`order_detail_id`),
  KEY `product_key_idx` (`product_num`),
  KEY `fk_orderdetail_orders_idx` (`order_num`),
  KEY `idx_orderdetail_product_date_amount` (`product_num`,`order_date`,`amount`),
  CONSTRAINT `fk_orderdetail_orders` FOREIGN KEY (`order_num`) REFERENCES `orders` (`order_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_key` FOREIGN KEY (`product_num`) REFERENCES `product` (`product_num`)
) ENGINE=InnoDB AUTO_INCREMENT=15381165 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ordernow`
--

DROP TABLE IF EXISTS `ordernow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordernow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_num` int unsigned NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK_Orders_TO_OrderNow` (`order_num`),
  CONSTRAINT `FK_Orders_TO_OrderNow` FOREIGN KEY (`order_num`) REFERENCES `orders` (`order_num`)
) ENGINE=InnoDB AUTO_INCREMENT=861 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_num` int unsigned NOT NULL,
  `order_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_num` int NOT NULL,
  `detail_address` varchar(100) DEFAULT NULL,
  `address` int NOT NULL,
  PRIMARY KEY (`order_num`),
  KEY `customer_key_idx` (`customer_num`),
  KEY `order_date_idx` (`order_date`),
  CONSTRAINT `customer_key` FOREIGN KEY (`customer_num`) REFERENCES `customer` (`customer_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_num` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `stock` int NOT NULL DEFAULT '1',
  `detail` varchar(100) DEFAULT NULL,
  `price` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_num`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-18 11:14:18
