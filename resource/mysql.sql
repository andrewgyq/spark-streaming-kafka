/*
Navicat MySQL Data Transfer

Source Server         : 准实时配置数据库
Source Server Version : 50623
Source Host           : 99.12.140.149:5322
Source Database       : rtsub

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2017-05-19 14:21:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `app_config`
-- ----------------------------
DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config` (
  `app_name` varchar(50) NOT NULL DEFAULT '',
  `zk_hosts` varchar(30) DEFAULT NULL,
  `kafka_input_brokers` varchar(30) DEFAULT NULL,
  `kafka_input_topics` varchar(100) DEFAULT NULL,
  `zk_path` varchar(20) DEFAULT NULL,
  `app_upgraded` varchar(10) DEFAULT NULL,
  `streaming_batch_duration` int(11) DEFAULT NULL,
  `app_hdfs_dir` varchar(30) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_config
-- ----------------------------
INSERT INTO `app_config` VALUES ('LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC', '99.12.167.105:24002', '99.12.156.134:9092', 'LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', '/kafkaOffset', 'false', '30', 'd:/config', 'A');

-- ----------------------------
-- Table structure for `app_smap`
-- ----------------------------
DROP TABLE IF EXISTS `app_smap`;
CREATE TABLE `app_smap` (
  `app_name` varchar(50) NOT NULL DEFAULT '',
  `table_name` varchar(50) NOT NULL DEFAULT '',
  `table_column` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`app_name`,`table_name`,`table_column`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_smap
-- ----------------------------
INSERT INTO `app_smap` VALUES ('LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC', 'SCFG_VBS_BBK_DST_BBK_MAP', 'BBK_ORG_ID');

-- ----------------------------
-- Table structure for `app_topic_column`
-- ----------------------------
DROP TABLE IF EXISTS `app_topic_column`;
CREATE TABLE `app_topic_column` (
  `app_name` varchar(50) NOT NULL DEFAULT '',
  `table_name` varchar(50) DEFAULT NULL,
  `table_column` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_topic_column
-- ----------------------------
INSERT INTO `app_topic_column` VALUES ('LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC', 'A6TXCDTAP', 'CTSBBKNBR');

-- ----------------------------
-- Table structure for `topic_stat`
-- ----------------------------
DROP TABLE IF EXISTS `topic_stat`;
CREATE TABLE `topic_stat` (
  `bbk_nbr` varchar(3) NOT NULL DEFAULT '',
  `app_name` varchar(50) NOT NULL DEFAULT '',
  `input_topic` varchar(100) DEFAULT NULL,
  `output_topic` varchar(100) DEFAULT NULL,
  `developer` varchar(20) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`bbk_nbr`,`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of topic_stat
-- ----------------------------
INSERT INTO `topic_stat` VALUES ('110', 'LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC', 'LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', 'RDP_ASUB_110_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', '374643', '2017-05-10', null, '2017-05-08', 'A');
INSERT INTO `topic_stat` VALUES ('127', 'LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC', 'LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', 'RDP_ASUB_127_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', '374643', '2017-05-10', null, '2017-05-08', 'A');
INSERT INTO `topic_stat` VALUES ('571', 'LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC', 'LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', 'RDP_ASUB_571_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', '374643', '2017-05-10', null, '2017-05-08', 'A');
INSERT INTO `topic_stat` VALUES ('755', 'LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC', 'LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', 'RDP_ASUB_755_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', '374643', '2017-05-10', null, '2017-05-08', 'A');
INSERT INTO `topic_stat` VALUES ('769', 'LV46_SUB_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC', 'LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', 'RDP_ASUB_769_LI08_A_CMB03_A3DTA_A6TXCDTAP_SRC_SZ', '374643', '2017-05-10', null, '2017-05-08', 'A');
