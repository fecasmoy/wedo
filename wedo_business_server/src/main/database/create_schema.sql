CREATE DATABASE IF NOT EXISTS wedo_bserver;
USE wedo_bserver;

--登录账户
DROP TABLE IF EXISTS `wd_user`;
CREATE TABLE `wd_user` (
  `id` BIGINT(20) UNSIGNED NOT NULL auto_increment,
  `guid` VARCHAR(50) NOT NULL,
  `nick_name` VARCHAR(50) NOT NULL,
  `user_name` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `user_type` TINYINT(3) UNSIGNED NOT NULL,
  `user_grade` INT(10) UNSIGNED NOT NULL,
  `create_time` INT(10) UNSIGNED NOT NULL,
  `last_login_time` INT(10) UNSIGNED NOT NULL,
  `user_remark` TEXT,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--账户详情
DROP TABLE IF EXISTS `wd_account`;
CREATE TABLE `wd_account` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `guid` VARCHAR(50) NOT NULL DEFAULT '0',
  `real_name` VARCHAR(50) NOT NULL DEFAULT '0',
  `paper_type` TINYINT UNSIGNED NOT NULL DEFAULT '0',
  `paper_id` VARCHAR(50) NOT NULL DEFAULT '0',
  `gender` TINYINT UNSIGNED NOT NULL DEFAULT '0',
  `birthday` INT UNSIGNED NOT NULL DEFAULT '0',
  `contact_address` TEXT NOT NULL,
  `phone` VARCHAR(50) NOT NULL DEFAULT '0',
  `cell_phone` VARCHAR(50) NOT NULL DEFAULT '0',
  `email` VARCHAR(50) NOT NULL DEFAULT '0',
  `account_sign` TEXT NOT NULL,
  `remark` TEXT NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--送货地址
DROP TABLE IF EXISTS `wd_deliver_info`;
CREATE TABLE `wd_deliver_info` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `guid` VARCHAR(50) NOT NULL,
  `alias` VARCHAR(50) NOT NULL,
  `contact_name` VARCHAR(50) NOT NULL,
  `contact_phone` VARCHAR(50) NOT NULL,
  `address` TEXT NOT NULL,
  `remark` TEXT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--商品类目
DROP TABLE IF EXISTS `wd_goods_classification`;
CREATE TABLE `wd_goods_classification` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `guid` VARCHAR(50) NOT NULL,
  `class_name` VARCHAR(50) NOT NULL,
  `class_parent` VARCHAR(50) NOT NULL,
  `class_type` VARCHAR(50) NOT NULL,
  `description` TEXT NOT NULL,
  `remark` TEXT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--商品详情
DROP TABLE IF EXISTS `wd_goods_detail`;
CREATE TABLE `wd_goods_detail` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `guid` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `class_guid` VARCHAR(50) NOT NULL,
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  `upcart_time` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `downcart_time` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `goods_pic` TEXT NOT NULL,
  `goods_info_url` TEXT NOT NULL,
  `goods_description` TEXT NOT NULL,
  `remark` TEXT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--商品评价evaluation
DROP TABLE IF EXISTS `wd_goods_evaluation`;
CREATE TABLE `wd_goods_evaluation` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_guid` VARCHAR(50) NOT NULL,
  `goods_guid` VARCHAR(50) NOT NULL,
  `evalue_time` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `evaluation` TEXT NOT NULL,
  `evaluation_pic_urls` TEXT NOT NULL,
  `score` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  `remark` TEXT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--订单
DROP TABLE IF EXISTS `wd_order`;
CREATE TABLE `wd_order` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `guid` VARCHAR(50) NOT NULL,
  `user_guid` VARCHAR(50) NOT NULL,
  `order_type` TINYINT(3) UNSIGNED NOT NULL,
  `order_status` TINYINT(3) UNSIGNED NOT NULL,
  `goods_guid` VARCHAR(50) NOT NULL,
  `create_time` INT(10) UNSIGNED NOT NULL,
  `finish_time` INT(10) UNSIGNED NOT NULL,
  `remark` TEXT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--订单跟踪
DROP TABLE IF EXISTS `wd_order_trace`;
CREATE TABLE `wd_order_trace` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_guid` VARCHAR(50) NOT NULL,
  `operate_time` INT(10) UNSIGNED NOT NULL,
  `order_status` TINYINT(3) UNSIGNED NOT NULL,
  `location` VARCHAR(50) NOT NULL,
  `operator` VARCHAR(50) NOT NULL,
  `operate_description` TEXT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--审核记录
DROP TABLE IF EXISTS `wd_goods_review`;
CREATE TABLE `wd_goods_detail` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `guid` varchar(32) NOT NULL,
  --审核人
  --审核时间
  --审核备注信息
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--投诉记录
DROP TABLE IF EXISTS `wd_goods_detail`;
CREATE TABLE `wd_goods_detail` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `guid` varchar(32) NOT NULL,
  --名称
  --上架时间
  --下架时间
  --商品状态
  --商品类目guid
  --商品图片
  --商品描述
  --商品其他信息
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
