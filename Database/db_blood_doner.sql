/*
SQLyog Job Agent Version 10.0 Beta1 Copyright(c) Webyog Inc. All Rights Reserved.


MySQL - 5.5.5-10.1.9-MariaDB : Database - blood_doner
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Database structure for database `blood_doner` */

CREATE DATABASE /*!32312 IF NOT EXISTS*/`blood_doner` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `blood_doner`;

/*Table structure for table `admin_tbl` */

DROP TABLE IF EXISTS `admin_tbl`;

CREATE TABLE `admin_tbl` (
  `admin_id` int(5) NOT NULL AUTO_INCREMENT,
  `admin_user_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `admin_password` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `admin_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `admin_tbl` */

insert  into `admin_tbl` values (1,'admin','pass','admin@gmail.com');

/*Table structure for table `blood_bank_tbl` */

DROP TABLE IF EXISTS `blood_bank_tbl`;

CREATE TABLE `blood_bank_tbl` (
  `bb_id` int(11) NOT NULL AUTO_INCREMENT,
  `bb_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bb_address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bb_email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bb_contact_no` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`bb_id`),
  KEY `city_id` (`city_id`),
  CONSTRAINT `blood_bank_tbl_ibfk_1` FOREIGN KEY (`city_id`) REFERENCES `city_tbl` (`city_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `blood_bank_tbl` */

insert  into `blood_bank_tbl` values (1,'Saidu Blood Bank6','saidu road manglor swat','bloodbank@gmail.com','0374848027',1),(2,'Saidu Blood Bank7','saidu road mingora swat ','bloodbank@gmail.com','0374848027',1),(8,'kidney','mingora swat','abc123@gmail.com','03457547251',15),(9,'hassan medical complex','saidu road mingora swat','hassan@gmail.com','03595958859',1),(10,'hassan medical complex','saidu road mingora swat','hassan@gmail.com','03595958859',1);

/*Table structure for table `blood_group_tbl` */

DROP TABLE IF EXISTS `blood_group_tbl`;

CREATE TABLE `blood_group_tbl` (
  `blood_group_id` int(11) NOT NULL AUTO_INCREMENT,
  `blood_group` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`blood_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `blood_group_tbl` */

insert  into `blood_group_tbl` values (1,'A+'),(2,'A-'),(3,'B+'),(4,'B-'),(5,'AB+'),(6,'AB-'),(7,'O+'),(8,'O-');

/*Table structure for table `city_tbl` */

DROP TABLE IF EXISTS `city_tbl`;

CREATE TABLE `city_tbl` (
  `city_id` int(11) NOT NULL AUTO_INCREMENT,
  `city_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `city_tbl` */

insert  into `city_tbl` values (1,'Saidu Sharif'),(12,'Islamabad'),(14,'Matta'),(15,'mingora'),(16,'Karachi\r'),(17,'Lahore\r'),(18,'Faisalābād\r'),(19,'Serai\r'),(20,'Rāwalpindi\r'),(21,'Multān\r'),(22,'Gujrānwāla\r'),(23,'Hyderābād City\r'),(24,'Peshāwar\r'),(25,'Abbottābād\r'),(26,'Islamabad\r'),(27,'Quetta\r'),(28,'Bannu\r'),(29,'Bahāwalpur\r'),(30,'Sargodha\r'),(31,'Siālkot City\r'),(32,'Sukkur\r'),(33,'Lārkāna\r'),(34,'Sheikhupura\r'),(35,'Mīrpur Khās\r'),(36,'Rahīmyār Khān\r'),(37,'Kohāt\r'),(38,'Jhang Sadr\r'),(39,'Gujrāt\r'),(40,'Bardār\r'),(41,'Kasūr\r'),(42,'Dera Ghāzi Khān\r'),(43,'Masīwāla\r'),(44,'Nawābshāh\r'),(45,'Okāra\r'),(46,'Gilgit\r'),(47,'Chiniot\r'),(48,'Sādiqābād\r'),(49,'Turbat\r'),(50,'Dera Ismāīl Khān\r'),(51,'Chaman\r'),(52,'Zhob\r'),(53,'Mehra\r'),(54,'Parachinār\r'),(55,'Gwādar\r'),(56,'Kundiān\r'),(57,'Shahdād Kot\r'),(58,'Harīpur\r'),(59,'Matiāri\r'),(60,'Dera Allāhyār\r'),(61,'Lodhrān\r'),(62,'Batgrām\r'),(63,'Thatta\r'),(64,'Bāgh\r'),(65,'Badīn\r'),(66,'Mānsehra\r'),(67,'Ziārat\r'),(68,'Muzaffargarh\r'),(69,'Tando Allāhyār\r'),(70,'Dera Murād Jamāli\r'),(71,'Karak\r'),(72,'Mardan\r'),(73,'Uthal\r'),(74,'Nankāna Sāhib\r'),(75,'Bārkhān\r'),(76,'Hāfizābād\r'),(77,'Kotli\r'),(78,'Loralai\r'),(79,'Dera Bugti\r'),(80,'Jhang City\r'),(81,'Sāhīwāl\r'),(82,'Sānghar\r'),(83,'Pākpattan\r'),(84,'Chakwāl\r'),(85,'Khushāb\r'),(86,'Ghotki\r'),(87,'Kohlu\r'),(88,'Khuzdār\r'),(89,'Awārān\r'),(90,'Nowshera\r'),(91,'Chārsadda\r'),(92,'Qila Abdullāh\r'),(93,'Bahāwalnagar\r'),(94,'Dādu\r'),(95,'Alīābad\r'),(96,'Lakki Marwat\r'),(97,'Chilās\r'),(98,'Pishin\r'),(99,'Tānk\r'),(100,'Chitrāl\r'),(101,'Qila Saifullāh\r'),(102,'Shikārpur\r'),(103,'Panjgūr\r'),(104,'Mastung\r'),(105,'Kalāt\r'),(106,'Gandāvā\r'),(107,'Khānewāl\r'),(108,'Nārowāl\r'),(109,'Khairpur\r'),(110,'Malakand\r'),(111,'Vihāri\r'),(112,'Saidu Sharif\r'),(113,'Jhelum\r'),(114,'Mandi Bahāuddīn\r'),(115,'Bhakkar\r'),(116,'Toba Tek Singh\r'),(117,'Jāmshoro\r'),(118,'Khārān\r'),(119,'Umarkot\r'),(120,'Hangu\r'),(121,'Timargara\r'),(122,'Gākuch\r'),(123,'Jacobābād\r'),(124,'Alpūrai\r'),(125,'Miānwāli\r'),(126,'Mūsa Khel Bāzār\r'),(127,'Naushahro Fīroz\r'),(128,'New Mīrpur\r'),(129,'Daggar\r'),(130,'Eidgāh\r'),(131,'Sibi\r'),(132,'Dālbandīn\r'),(133,'Rājanpur\r'),(134,'Leiah\r'),(135,'Upper Dir\r'),(136,'Tando Muhammad Khān\r'),(137,'Attock City\r'),(138,'Rāwala Kot\r'),(139,'Swābi\r'),(140,'Kandhkot\r'),(141,'Dasu\r'),(142,'Athmuqam\r'),(143,'mingora');

/*Table structure for table `feedback_tbl` */

DROP TABLE IF EXISTS `feedback_tbl`;

CREATE TABLE `feedback_tbl` (
  `feedback_id` int(11) NOT NULL AUTO_INCREMENT,
  `feedback_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `feedback_email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `feedback_msg` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `feedback_status` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`feedback_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `feedback_tbl` */

insert  into `feedback_tbl` values (1,'ali','ali@gmail.com','pass',1),(2,'noman','abgfa123@gmail.com','dgdjdusgsgsjshg',1);

/*Table structure for table `location_tbl` */

DROP TABLE IF EXISTS `location_tbl`;

CREATE TABLE `location_tbl` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `blood_id` int(11) DEFAULT NULL,
  `location_lat` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location_lon` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `time` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`location_id`),
  KEY `user_id` (`user_id`),
  KEY `blood_id` (`blood_id`),
  CONSTRAINT `location_tbl_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `location_tbl_ibfk_2` FOREIGN KEY (`blood_id`) REFERENCES `blood_group_tbl` (`blood_group_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `location_tbl` */

insert  into `location_tbl` values (28,10,1,'34.7720116','72.3599768','14-09-2020','04:16:11'),(29,14,1,'34.7715264','72.362829','27-09-2019','11:43:38'),(30,19,7,'34.765992','72.362829','05-10-2019','01:44:12'),(31,26,1,'34.7719529','72.3599891','07-09-2020','12:15:17');

/*Table structure for table `user_tbl` */

DROP TABLE IF EXISTS `user_tbl`;

CREATE TABLE `user_tbl` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL,
  `user_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_password` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_contact_no` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_age` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_gender` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `blood_id` int(11) DEFAULT NULL,
  `user_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_last_donation` varchar(30) COLLATE utf8_unicode_ci DEFAULT '0',
  `user_diseas_status` tinyint(4) DEFAULT '0',
  `user_diseas` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_no_donation` int(3) DEFAULT '0',
  `user_donation_notification` tinyint(3) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `city_id` (`city_id`),
  KEY `blood_id` (`blood_id`),
  CONSTRAINT `user_tbl_ibfk_1` FOREIGN KEY (`city_id`) REFERENCES `city_tbl` (`city_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_tbl_ibfk_2` FOREIGN KEY (`blood_id`) REFERENCES `blood_group_tbl` (`blood_group_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `user_tbl` */

insert  into `user_tbl` values (10,'Hassan khan',1,'hassan@gmail.com','pass','03441897099','1998-07-20','Male',1,'IMG-20181020-WA00272541.jpg','2019-05-01',0,'No Diseas',4,1),(12,'zeeshan',1,'zeeshan@gmail.com','pass','03448484i84 ','1978-07-20','Male',1,'30953.','0',0,'somebdiseas',0,0),(14,'hzhs',1,'hehe','hshs','fjfiur','2019-08-01','Male',1,'32647.','0',0,'No Diseas',0,0),(15,'hshsh',1,'hdh','shhs','dhhdhs','2019-08-01','Male',1,NULL,'2019-04-07',0,'No Diseas',0,0),(16,'hahahahha',1,'ahahhahah','pass','hsjajshhs','2019-08-01','Male',1,NULL,'0',0,'No Diseas',0,0),(18,'hshshsu',1,'jan','pass','shshhs','2019-08-01','Male',1,'IMG-20190730-WA000528282.jpg','0',0,'No Diseas',0,0),(19,'Kashif',1,'kashif@gmail.com','pass','03441897099','1997-08-14','Male',7,NULL,'0',0,'No Diseas',0,0),(20,'noman',1,'nomics26@gmail.com','swat12345','03479093696','1996-11-10','Male',1,'IMG-20181120-WA000532478.jpg','0',0,'No Diseas',0,0),(22,'hshsh',14,'ajja@gmail.com','pass','nxjzhs','2019-09-03','Male',1,NULL,'0',0,'No Diseas',0,0),(25,'hdghh',1,'dhhx','1','467','1992-09-07','Male',6,NULL,'0',0,'No Diseas',0,0),(26,'Aman khan',38,'aman@gmail.com','pass','03441897099','1999-09-07','Male',1,'IMG_20200823_173843_323166.jpg','2020-09-07',0,'No Diseas',1,0),(27,'Ali',38,'ali@gmail.com','pass','03726275','2000-09-15','Male',1,'IMG_20200823_173750_45791.jpg','0',0,'No Diseas',0,0),(28,'hdfud',110,'hwhfhe@gmail.com','12345','052572848','2020-10-01','Male',1,'Screenshot_20201013-162202_One UI Home7965.jpg','0',0,'No Diseas',0,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
