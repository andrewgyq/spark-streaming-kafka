-- ----------------------------
-- Table structure for `app_config`
-- 应用的配置信息
-- ----------------------------
DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config` (
  `app_name` varchar(50) NOT NULL DEFAULT ''COMMENT '应用名称',
  `zk_hosts` varchar(30) DEFAULT NULL COMMENT 'Zookeeper地址',
  `zk_path` varchar(20) DEFAULT NULL COMMENT 'Zookeeper路径',
  `kafka_input_brokers` varchar(30) DEFAULT NULL COMMENT 'Kafka地址',
  `kafka_input_topics` varchar(100) DEFAULT NULL COMMENT '待拆分topic',
  `app_upgraded` varchar(10) DEFAULT NULL COMMENT '应用升级',
  `streaming_batch_duration` int(11) DEFAULT NULL COMMENT '读取Kafka数据周期单位秒',
  `app_hdfs_dir` varchar(30) DEFAULT NULL COMMENT 'HDFS地址',
  `status` varchar(1) DEFAULT NULL COMMENT '应用状态',
  PRIMARY KEY (`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `app_smap`
-- 记录应用源表、字段与SMAP表、字段关联信息
-- 一个源表的多个字段会和多个SMAP表多个字段关联
-- ----------------------------
DROP TABLE IF EXISTS `app_smap`;
CREATE TABLE `app_smap` (
  `app_name` varchar(50) NOT NULL DEFAULT ''COMMENT '应用名称',
  `src_table` varchar(50) NOT NULL DEFAULT ''COMMENT '源表名称',
  `src_column` varchar(20) NOT NULL DEFAULT ''COMMENT '源表字段',
  `smap_table` varchar(50) NOT NULL DEFAULT ''COMMENT 'SMAP表名称',
  `smap_column` varchar(20) NOT NULL DEFAULT '' COMMENT 'SMAP表字段'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `topic_stat`
-- 记录应用拆分信息
-- ----------------------------
DROP TABLE IF EXISTS `topic_stat`;
CREATE TABLE `topic_stat` (
  `bbk_nbr` varchar(3) NOT NULL DEFAULT '' COMMENT '分行',
  `app_name` varchar(50) NOT NULL DEFAULT '' COMMENT '应用名称',
  `input_topic` varchar(100) DEFAULT NULL COMMENT '拆分topic',
  `output_topic` varchar(100) DEFAULT NULL COMMENT '结果topic',
  `developer` varchar(20) DEFAULT NULL COMMENT '负责人',
  `start_date` date DEFAULT NULL COMMENT '上线日期',
  `end_date` date DEFAULT NULL COMMENT '下线日期',
  `create_date` date DEFAULT NULL COMMENT '创建日期',
  `status` varchar(1) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`bbk_nbr`,`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
