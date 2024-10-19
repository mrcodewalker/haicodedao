-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.27-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for codewalker
CREATE DATABASE IF NOT EXISTS `codewalker` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `codewalker`;

-- Dumping structure for table codewalker.users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `provider_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `role_id` bigint(20) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `github_id` varchar(100) DEFAULT '',
  `avatar` varchar(1000) DEFAULT 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `FK_Role` (`role_id`),
  CONSTRAINT `FK_Role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `CONSTRAINT_1` CHECK (octet_length(`password`) >= 8 or `password` is null)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table codewalker.users: ~13 rows (approximately)
INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `provider_name`, `created_at`, `updated_at`, `role_id`, `is_active`, `github_id`, `avatar`) VALUES
	(1, 'haicodedao', '', '$2a$10$cJJcCCawrMKi5NbvXPHuYOWmJBgh7h/JafGEayn6c1MUVTnENdia.', 'KMA Legend', '2024-08-31 07:44:34', '2024-09-04 17:21:08', 2, 1, '', 'https://img.icons8.com/?size=100&id=13042&format=png&color=000000'),
	(2, 'haideptrai', 'mr.haihuynhngoc@gmail.com', '$2a$10$SAvEl4NMuWmSRvyRMAAijuGBcg6mGiL7cWQwputR/hy0EIfzaFkh2', 'KMA Legend', '2024-09-02 06:59:35', '2024-09-05 13:22:59', 1, 1, '', 'https://img.icons8.com/?size=100&id=rxNOMfQCjtXW&format=png&color=000000'),
	(3, 'haisieudeptrai', 'abc@gmail.com', '$2a$10$aAwo0mwl2WRGO8373aD9xeypwWO0caIkiv0WSJdHBP4VynFglr/US', 'KMA Legend', '2024-09-02 07:03:05', '2024-09-02 07:03:05', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(4, 'hihunhngc65', 'mr.codewalker@gmail.com', '$2a$10$DtzgGMXp9yRWFLvJ3yEZJ.0RAv.g5sBTe5NIyFtsBjwXtOU4tsLKS', 'Google', '2024-09-02 18:24:42', '2024-09-02 18:26:06', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(5, 'haihuynhngoc79', 'mr.haimaris@gmail.com', '$2a$10$rSPMKTcEfxsmg7GUmrtrPe.1biHa/K39qvOlcMCMpGV7OanVlN7t6', 'Google', '2024-09-02 18:28:17', '2024-09-02 18:29:14', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(6, 'haihuynhngoc23', 'mr.haipig@gmail.com', '$2a$10$xjNYi5Rr8Tb4IB8PVuCfMO1FxQFxKBMMdiPuFzXDp2EtUO/XFu/c6', 'Google', '2024-09-02 18:44:31', '2024-09-02 18:46:04', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(7, 'mrsea73', 'mr.sea2k4@gmail.com', '$2a$10$4/DziWA8JOCTNbbz1uJL2.KPcsdW/uuxStY2fXJGOpincOFKJsqXq', 'Google', '2024-09-02 18:45:34', '2024-09-02 18:46:05', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(8, 'beoentertainment37', 'mr.haidevil@gmail.com', '$2a$10$0M12LZCk.MX3jxPqfrNcPuDyZQH4pBCscaEo4F1uLiDbw8ntgmkUG', 'Google', '2024-09-02 18:53:24', '2024-09-02 19:03:36', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(9, 'haihuynhngoc72', 'mr.haiseal@gmail.com', '$2a$10$F1ONlRY4IkjXeGgWXdI2zuelY2i5LgWwm6fZYV6PBdrqcuX1aMD5a', 'Google', '2024-09-02 19:03:08', '2024-09-03 10:40:23', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(10, 'haihuynhngoc41', 'mr.haibeu@gmail.com', '$2a$10$t/xKvpjpGGh2IPiqVECLLegRqcvvtJdlqNXE6kriXTGlHFUPL/szq', 'Google', '2024-09-02 19:14:49', '2024-09-03 10:40:25', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(11, 'haihuynhngoc60', 'haiteo9a1@gmail.com', '$2a$10$sUTnobyOYoogZs/Pe3PpV.4.QHuSKJFTyZMM5nALS55kLKBvRJezS', 'Google', '2024-09-02 19:35:30', '2024-09-03 10:40:26', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(12, 'haicarry78', 'huynhngochai12334@gmail.com', '$2a$10$J.QHETSswXtjF1RHfqEb7O/FDD1OQt4S/FJOA5MbD56f5GGPR.JyK', 'Google', '2024-09-03 10:45:06', '2024-09-03 10:45:06', 2, 1, '', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000'),
	(13, 'mrcodewalker25', '', '$2a$10$97PSPBRWrLDVLK1f6tZdj.0POKUAEZVjMQUHnnE.YpOAm58BEqT8y', 'Github', '2024-09-03 12:37:52', '2024-09-03 12:37:52', 2, 1, '136473311', 'https://img.icons8.com/?size=100&id=aVI7R6wBB2ge&format=png&color=000000');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
