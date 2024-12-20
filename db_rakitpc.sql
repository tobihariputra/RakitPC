-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for db_rakitpc
DROP DATABASE IF EXISTS `db_rakitpc`;
CREATE DATABASE IF NOT EXISTS `db_rakitpc` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_rakitpc`;

-- Dumping structure for table db_rakitpc.customer
DROP TABLE IF EXISTS `customer`;
CREATE TABLE IF NOT EXISTS `customer` (
  `idmember` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `alamat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `totalbiayarakit` double DEFAULT NULL,
  PRIMARY KEY (`idmember`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_rakitpc.customer: ~2 rows (approximately)
INSERT INTO `customer` (`idmember`, `nama`, `alamat`, `totalbiayarakit`) VALUES
	('C00001', 'tobiea', 'tki', 76032320),
	('C00002', 'toni', 'tci', 87248000),
	('C00003', 'edo', 'thi', 21040320),
	('C00004', 'abwert', 'koper', 0);

-- Dumping structure for table db_rakitpc.komponen
DROP TABLE IF EXISTS `komponen`;
CREATE TABLE IF NOT EXISTS `komponen` (
  `kategori` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `kodekomponen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `namakomponen` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `socket` varchar(50) DEFAULT NULL,
  `jenismemori` varchar(50) DEFAULT NULL,
  `harga` double DEFAULT NULL,
  `stok` int DEFAULT NULL,
  `gambar` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`kodekomponen`),
  CONSTRAINT `komponen_chk_1` CHECK ((`harga` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_rakitpc.komponen: ~19 rows (approximately)
INSERT INTO `komponen` (`kategori`, `kodekomponen`, `namakomponen`, `brand`, `socket`, `jenismemori`, `harga`, `stok`, `gambar`) VALUES
	('Casing PC', 'CASE-001', 'NZXT H510 Series', NULL, NULL, NULL, 1200000, 93, 'img\\nzxt_h510.png'),
	('CPU Cooler', 'COOL-001', 'Cooler Master Hyper 212', NULL, NULL, NULL, 600000, 93, 'img\\hyper_212.png'),
	('Motherboard', 'MOBO-001', 'MSI MPG X670E Carbon Wifi', 'AMD', 'AM5', 'DDR5', 4000000, 97, 'img\\msi_x670e.png'),
	('Motherboard', 'MOBO-002', 'ASUS TUF B450M-PLUS II GAMING', 'AMD', 'AM4', 'DDR4', 1500000, 98, 'img\\asus_b450.png'),
	('Motherboard', 'MOBO-003', 'GIGABYTE Z590 AORUS MASTER', 'Intel', 'LGA1200', 'DDR4', 3200000, 99, 'img\\gigabyte_z590.png'),
	('Motherboard', 'MOBO-004', 'B760M D3HP WIFI6', 'Intel', 'LGA1700', 'DDR5', 2300000, 23, 'img\\B760M.png'),
	('Processor', 'PROC-001', 'i3-10100', 'Intel', 'LGA1200', NULL, 1100000, 99, 'img\\i3_10100.png'),
	('Processor', 'PROC-002', 'i3-12100', 'Intel', 'LGA1700', NULL, 1500000, 99, 'img\\i3_12100.png'),
	('Processor', 'PROC-003', 'Ryzen 5 5600X', 'AMD', 'AM4', NULL, 3000000, 98, 'img\\ryzen5_5600x.png'),
	('Processor', 'PROC-004', 'i5-10400', 'Intel', 'LGA1200', NULL, 2500000, 100, 'img\\i5_10400.png'),
	('Processor', 'PROC-005', 'Ryzen 5-7500f', 'AMD', 'AM5', NULL, 2300000, 97, 'img\\ryzen5_7500f.jpg'),
	('Power Supply', 'PSU-001', 'Corsair RM750', NULL, NULL, NULL, 1800000, 93, 'img\\corsair_rm750.png'),
	('RAM', 'RAM-001', 'Corsair Vengeance 16GB 3600Mhz', NULL, NULL, 'DDR4', 1200000, 98, 'img\\corsair_vengeance.png'),
	('RAM', 'RAM-002', 'G.Skill Trident Z 16GB 3600Mhz', NULL, NULL, 'DDR4', 1400000, 99, 'img\\gskill_tridentz.png'),
	('RAM', 'RAM-003', 'ADATA XPG LANCER 16GB 6400 MT/s', NULL, NULL, 'DDR5', 1386000, 93, 'img\\adata_xpg.png'),
	('Storage', 'STOR-001', 'Samsung 970 Evo Plus 1TB', NULL, NULL, NULL, 2000000, 93, 'img\\samsung_970evo.png'),
	('Storage', 'STOR-002', 'WD Blue 500GB SSD', NULL, NULL, NULL, 800000, 100, 'img\\wd_blue.png'),
	('VGA', 'VGA-001', 'NVIDIA RTX 3060', NULL, NULL, NULL, 8000000, 95, 'img\\rtx_3060.png'),
	('VGA', 'VGA-002', 'AMD RX 6700 XT', NULL, NULL, NULL, 7500000, 98, 'img\\rx_6700xt.png');

-- Dumping structure for table db_rakitpc.rakit
DROP TABLE IF EXISTS `rakit`;
CREATE TABLE IF NOT EXISTS `rakit` (
  `norakit` int NOT NULL AUTO_INCREMENT,
  `tanggal` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`norakit`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_rakitpc.rakit: ~24 rows (approximately)
INSERT INTO `rakit` (`norakit`, `tanggal`) VALUES
	(1, '2024-12-16 15:05:58'),
	(2, '2024-12-16 15:07:16'),
	(3, '2024-12-16 15:14:16'),
	(4, '2024-12-16 15:22:29'),
	(5, '2024-12-16 15:28:44'),
	(6, '2024-12-16 15:30:29'),
	(8, '2024-12-16 15:39:52'),
	(9, '2024-12-16 15:40:13'),
	(10, '2024-12-16 15:41:44'),
	(11, '2024-12-16 16:06:32'),
	(12, '2024-12-16 16:30:30'),
	(13, '2024-12-16 00:00:00'),
	(14, '2024-12-16 00:00:00'),
	(15, '2024-12-16 00:00:00'),
	(16, '2024-12-16 00:00:00'),
	(17, '2024-12-16 00:00:00'),
	(18, '2024-12-16 00:00:00'),
	(20, '2024-12-17 00:00:00'),
	(21, '2024-12-17 00:00:00'),
	(22, '2024-12-17 12:12:55'),
	(23, '2024-12-17 12:17:27'),
	(24, '2024-12-17 19:08:24'),
	(25, '2024-12-17 19:48:04'),
	(26, '2024-12-20 11:36:14');

-- Dumping structure for table db_rakitpc.rakit_detail
DROP TABLE IF EXISTS `rakit_detail`;
CREATE TABLE IF NOT EXISTS `rakit_detail` (
  `norakit` int NOT NULL,
  `kodekomponen` varchar(50) NOT NULL,
  `jumlah` int DEFAULT '1',
  `subtotal` double DEFAULT NULL,
  PRIMARY KEY (`norakit`,`kodekomponen`),
  KEY `kodekomponen` (`kodekomponen`),
  CONSTRAINT `rakit_detail_ibfk_1` FOREIGN KEY (`norakit`) REFERENCES `rakit` (`norakit`) ON DELETE CASCADE,
  CONSTRAINT `rakit_detail_ibfk_2` FOREIGN KEY (`kodekomponen`) REFERENCES `komponen` (`kodekomponen`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_rakitpc.rakit_detail: ~192 rows (approximately)
INSERT INTO `rakit_detail` (`norakit`, `kodekomponen`, `jumlah`, `subtotal`) VALUES
	(1, 'CASE-001', 1, 1200000),
	(1, 'COOL-001', 1, 600000),
	(1, 'MOBO-002', 1, 1500000),
	(1, 'PROC-003', 1, 3000000),
	(1, 'PSU-001', 1, 1800000),
	(1, 'RAM-002', 1, 1400000),
	(1, 'STOR-001', 1, 2000000),
	(1, 'VGA-001', 1, 8000000),
	(2, 'CASE-001', 1, 1200000),
	(2, 'COOL-001', 1, 600000),
	(2, 'MOBO-003', 1, 3200000),
	(2, 'PROC-001', 1, 1100000),
	(2, 'PSU-001', 1, 1800000),
	(2, 'RAM-001', 1, 1200000),
	(2, 'STOR-001', 1, 2000000),
	(2, 'VGA-001', 1, 8000000),
	(3, 'CASE-001', 1, 1200000),
	(3, 'COOL-001', 1, 600000),
	(3, 'MOBO-002', 1, 1500000),
	(3, 'PROC-003', 1, 3000000),
	(3, 'PSU-001', 1, 1800000),
	(3, 'RAM-001', 1, 1200000),
	(3, 'STOR-001', 1, 2000000),
	(3, 'VGA-001', 1, 8000000),
	(4, 'CASE-001', 1, 1200000),
	(4, 'COOL-001', 1, 600000),
	(4, 'MOBO-002', 1, 1500000),
	(4, 'PROC-003', 1, 2300000),
	(4, 'PSU-001', 1, 1800000),
	(4, 'RAM-001', 1, 1200000),
	(4, 'STOR-001', 1, 2000000),
	(4, 'VGA-001', 1, 8000000),
	(5, 'CASE-001', 1, 1200000),
	(5, 'COOL-001', 1, 600000),
	(5, 'MOBO-003', 1, 3200000),
	(5, 'PROC-001', 1, 1100000),
	(5, 'PSU-001', 1, 1800000),
	(5, 'RAM-001', 1, 1200000),
	(5, 'STOR-001', 1, 2000000),
	(5, 'VGA-001', 1, 8000000),
	(6, 'CASE-001', 1, 1200000),
	(6, 'COOL-001', 1, 600000),
	(6, 'MOBO-002', 1, 1500000),
	(6, 'PROC-003', 1, 3000000),
	(6, 'PSU-001', 1, 1800000),
	(6, 'RAM-001', 1, 1200000),
	(6, 'STOR-001', 1, 2000000),
	(6, 'VGA-001', 1, 8000000),
	(8, 'CASE-001', 1, 1200000),
	(8, 'COOL-001', 1, 600000),
	(8, 'MOBO-002', 1, 1500000),
	(8, 'PROC-003', 1, 3000000),
	(8, 'PSU-001', 1, 1800000),
	(8, 'RAM-001', 1, 1200000),
	(8, 'STOR-001', 1, 2000000),
	(8, 'VGA-001', 1, 8000000),
	(9, 'CASE-001', 1, 1200000),
	(9, 'COOL-001', 1, 600000),
	(9, 'MOBO-003', 1, 3200000),
	(9, 'PROC-004', 1, 2500000),
	(9, 'PSU-001', 1, 1800000),
	(9, 'RAM-001', 1, 1200000),
	(9, 'STOR-001', 1, 2000000),
	(9, 'VGA-001', 1, 8000000),
	(10, 'CASE-001', 1, 1200000),
	(10, 'COOL-001', 1, 600000),
	(10, 'MOBO-003', 1, 3200000),
	(10, 'PROC-001', 1, 1100000),
	(10, 'PSU-001', 1, 1800000),
	(10, 'RAM-002', 2, 2800000),
	(10, 'STOR-001', 2, 4000000),
	(10, 'VGA-001', 1, 8000000),
	(11, 'CASE-001', 1, 1200000),
	(11, 'COOL-001', 1, 600000),
	(11, 'MOBO-002', 1, 1500000),
	(11, 'PROC-003', 1, 3000000),
	(11, 'PSU-001', 1, 1800000),
	(11, 'RAM-001', 1, 1200000),
	(11, 'STOR-001', 1, 2000000),
	(11, 'VGA-002', 1, 7500000),
	(12, 'CASE-001', 1, 1200000),
	(12, 'COOL-001', 1, 600000),
	(12, 'MOBO-002', 1, 1500000),
	(12, 'PROC-003', 1, 3000000),
	(12, 'PSU-001', 1, 1800000),
	(12, 'RAM-001', 1, 1200000),
	(12, 'STOR-001', 1, 2000000),
	(12, 'VGA-001', 1, 8000000),
	(13, 'CASE-001', 1, 1200000),
	(13, 'COOL-001', 1, 600000),
	(13, 'MOBO-002', 1, 1500000),
	(13, 'PROC-003', 1, 3000000),
	(13, 'PSU-001', 1, 1800000),
	(13, 'RAM-001', 1, 1200000),
	(13, 'STOR-001', 1, 2000000),
	(13, 'VGA-001', 2, 16000000),
	(14, 'CASE-001', 1, 1200000),
	(14, 'COOL-001', 1, 600000),
	(14, 'MOBO-003', 1, 3200000),
	(14, 'PROC-004', 1, 2500000),
	(14, 'PSU-001', 1, 1800000),
	(14, 'RAM-001', 1, 1200000),
	(14, 'STOR-001', 1, 2000000),
	(14, 'VGA-002', 1, 7500000),
	(15, 'CASE-001', 1, 1200000),
	(15, 'COOL-001', 1, 600000),
	(15, 'MOBO-002', 1, 1500000),
	(15, 'PROC-003', 1, 3000000),
	(15, 'PSU-001', 1, 1800000),
	(15, 'RAM-002', 1, 1400000),
	(15, 'STOR-001', 1, 2000000),
	(15, 'VGA-002', 1, 7500000),
	(16, 'CASE-001', 1, 1200000),
	(16, 'COOL-001', 1, 600000),
	(16, 'MOBO-002', 1, 1500000),
	(16, 'PROC-003', 1, 3000000),
	(16, 'PSU-001', 1, 1800000),
	(16, 'RAM-002', 1, 1400000),
	(16, 'STOR-001', 1, 2000000),
	(16, 'VGA-001', 1, 8000000),
	(17, 'CASE-001', 1, 1200000),
	(17, 'COOL-001', 1, 600000),
	(17, 'MOBO-002', 1, 1500000),
	(17, 'PROC-003', 1, 2300000),
	(17, 'PSU-001', 1, 1800000),
	(17, 'RAM-001', 1, 1200000),
	(17, 'STOR-001', 1, 2000000),
	(17, 'VGA-001', 1, 8000000),
	(18, 'CASE-001', 1, 1200000),
	(18, 'COOL-001', 1, 600000),
	(18, 'MOBO-002', 1, 1500000),
	(18, 'PROC-003', 1, 3000000),
	(18, 'PSU-001', 1, 1800000),
	(18, 'RAM-001', 1, 1200000),
	(18, 'STOR-001', 1, 2000000),
	(18, 'VGA-001', 1, 8000000),
	(20, 'CASE-001', 1, 1200000),
	(20, 'COOL-001', 1, 600000),
	(20, 'MOBO-002', 1, 1500000),
	(20, 'PROC-003', 1, 3000000),
	(20, 'PSU-001', 1, 1800000),
	(20, 'RAM-001', 1, 1200000),
	(20, 'STOR-002', 1, 800000),
	(20, 'VGA-002', 1, 7500000),
	(21, 'CASE-001', 1, 1200000),
	(21, 'COOL-001', 1, 600000),
	(21, 'MOBO-002', 1, 1500000),
	(21, 'PROC-003', 1, 3000000),
	(21, 'PSU-001', 1, 1800000),
	(21, 'RAM-001', 1, 1200000),
	(21, 'STOR-001', 1, 2000000),
	(21, 'VGA-001', 1, 8000000),
	(22, 'CASE-001', 1, 1200000),
	(22, 'COOL-001', 1, 600000),
	(22, 'MOBO-003', 1, 3200000),
	(22, 'PROC-001', 1, 1100000),
	(22, 'PSU-001', 1, 1800000),
	(22, 'RAM-002', 1, 1400000),
	(22, 'STOR-001', 1, 2000000),
	(22, 'VGA-002', 1, 7500000),
	(23, 'CASE-001', 1, 1200000),
	(23, 'COOL-001', 1, 600000),
	(23, 'MOBO-001', 1, 4000000),
	(23, 'PROC-005', 1, 2300000),
	(23, 'PSU-001', 1, 1800000),
	(23, 'RAM-003', 1, 1386000),
	(23, 'STOR-001', 1, 2000000),
	(23, 'VGA-001', 1, 8000000),
	(24, 'CASE-001', 1, 1200000),
	(24, 'COOL-001', 1, 600000),
	(24, 'MOBO-001', 1, 4000000),
	(24, 'PROC-005', 1, 2300000),
	(24, 'PSU-001', 1, 1800000),
	(24, 'RAM-003', 4, 5544000),
	(24, 'STOR-001', 1, 2000000),
	(24, 'VGA-002', 1, 7500000),
	(25, 'CASE-001', 1, 1200000),
	(25, 'COOL-001', 1, 600000),
	(25, 'MOBO-002', 1, 1500000),
	(25, 'PROC-003', 1, 3000000),
	(25, 'PSU-001', 1, 1800000),
	(25, 'RAM-001', 1, 1200000),
	(25, 'STOR-001', 1, 2000000),
	(25, 'VGA-001', 1, 8000000),
	(26, 'CASE-001', 1, 1200000),
	(26, 'COOL-001', 1, 600000),
	(26, 'MOBO-004', 1, 2300000),
	(26, 'PROC-002', 1, 1500000),
	(26, 'PSU-001', 1, 1800000),
	(26, 'RAM-003', 1, 1386000),
	(26, 'STOR-001', 1, 2000000),
	(26, 'VGA-001', 1, 8000000);

-- Dumping structure for table db_rakitpc.transaksi
DROP TABLE IF EXISTS `transaksi`;
CREATE TABLE IF NOT EXISTS `transaksi` (
  `idtransaksi` varchar(10) NOT NULL,
  `idmember` varchar(10) NOT NULL,
  `norakit` int NOT NULL,
  `tanggal` datetime DEFAULT CURRENT_TIMESTAMP,
  `totalbayar` double NOT NULL,
  PRIMARY KEY (`idtransaksi`),
  KEY `idmember` (`idmember`),
  KEY `norakit` (`norakit`),
  CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`idmember`) REFERENCES `customer` (`idmember`),
  CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`norakit`) REFERENCES `rakit` (`norakit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_rakitpc.transaksi: ~7 rows (approximately)
INSERT INTO `transaksi` (`idtransaksi`, `idmember`, `norakit`, `tanggal`, `totalbayar`) VALUES
	('T00001', 'C00001', 8, '2024-12-17 00:00:00', 21616000),
	('T00002', 'C00001', 23, '2024-12-19 00:00:00', 23840320),
	('T00003', 'C00002', 20, '2024-12-18 17:53:30', 19712000),
	('T00004', 'C00002', 11, '2024-12-29 18:02:27', 21056000),
	('T00005', 'C00002', 10, '2024-12-06 18:03:09', 25424000),
	('T00006', 'C00002', 22, '2024-12-19 18:56:53', 21056000),
	('T00007', 'C00001', 13, '2024-12-21 16:52:17', 30576000),
	('T00008', 'C00003', 26, '2024-12-20 11:36:41', 21040320);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
