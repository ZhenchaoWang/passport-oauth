SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_oauth_app_info
-- ----------------------------
DROP TABLE IF EXISTS `t_oauth_app_info`;
CREATE TABLE `t_oauth_app_info` (
  `app_id` bigint(20) unsigned NOT NULL,
  `app_name` varchar(255) NOT NULL,
  `logo` varchar(1024) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `enable` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT 'app enable status, 0:disable, 1:enable',
  `redirect_uri` varchar(4096) DEFAULT NULL,
  `cancel_redirect_uri` varchar(4096) DEFAULT NULL,
  `scope` varchar(1024) DEFAULT NULL,
  `token_validity` int(11) NOT NULL DEFAULT '7776000' COMMENT 'seconds',
  `secret` varchar(255) NOT NULL,
  `level` int(2) NOT NULL DEFAULT '1',
  `creator_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_oauth_app_info
-- ----------------------------
INSERT INTO `t_oauth_app_info` VALUES ('2882303761517520186', '指间生活', 'https://github.com/interdigital-life/interdigital-life.github.io/blob/master/img/google-logo.jpg?raw=true', '0', 'http://www.zhenchao.com', null, '1 2 4 5', '7776000', 'empty', '1', '888888', '2017-01-21 15:53:37', '2017-02-28 22:33:27');

-- ----------------------------
-- Table structure for t_scope
-- ----------------------------
DROP TABLE IF EXISTS `t_scope`;
CREATE TABLE `t_scope` (
  `id` int(11) NOT NULL,
  `name` varchar(1024) NOT NULL,
  `description` varchar(2048) NOT NULL,
  `level` int(2) NOT NULL DEFAULT '1',
  `type` int(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_scope
-- ----------------------------
INSERT INTO `t_scope` VALUES ('1', '获取您的基本信息（昵称、头像）', '获取您的基本信息（昵称、头像）', '1', '1');
INSERT INTO `t_scope` VALUES ('2', '获取您的手机号码', '获取您的手机号码', '2', '1');
INSERT INTO `t_scope` VALUES ('3', '获取您的电子邮箱', '获取您的电子邮箱', '2', '1');
INSERT INTO `t_scope` VALUES ('4', '获取您在本APP的唯一标识（open id）', '获取您在本APP的唯一标识（open id）', '1', '1');
INSERT INTO `t_scope` VALUES ('5', '获取您在本公司的唯一标识（union id）', '获取您在本公司的唯一标识（union id）', '2', '1');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'user id',
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `age` int(10) unsigned NOT NULL DEFAULT '0',
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(1024) DEFAULT NULL,
  `avatar` varchar(1024) DEFAULT NULL COMMENT 'avatar url',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('100000', 'zhenchao', 'Bm+8geOCBsXZIM+FsWgaYQ==', '18', '13212345678', null, 'https://github.com/ZhenchaoWang/zhenchaowang.github.io/blob/master/img/zhenchao_100.jpg?raw=true');

-- ----------------------------
-- Table structure for t_user_app_authorization
-- ----------------------------
DROP TABLE IF EXISTS `t_user_app_authorization`;
CREATE TABLE `t_user_app_authorization` (
  `app_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `scope` varchar(1024) NOT NULL,
  `scope_sign` char(32) NOT NULL,
  `token_key` varchar(128) NOT NULL,
  `refresh_token_key` varchar(128) DEFAULT NULL,
  `refresh_token_expiration_time` bigint(20) DEFAULT NULL COMMENT 'milli seconds',
  `create_time` datetime NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `index_app_user_scope` (`app_id`,`user_id`,`scope_sign`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_app_authorization
-- ----------------------------
INSERT INTO `t_user_app_authorization` VALUES ('2882303761517520186', '100000', '1 4', '024c64526d52b0cfdf038dfa', 'KpM13xk3QXnDm0VhPvKqT9Mj86ZxmOXiwwwnsIfcr42AQVNhbd3YaJ0Id1TQLxjw', '8B2Y3EtPEIG0o2kF9VMKjg2jNghlaKXRD6tmogQhsfIMFufpmcKwkuzqN6dLqO6k', '31536000', '2017-02-11 18:25:02', '2017-02-25 12:13:09');
INSERT INTO `t_user_app_authorization` VALUES ('2882303761517520186', '100000', '1 2 4 5', 'e8b66581ac324fc39f031262', 'RBDo3sMk2wQvZ4nUEEsqnQeagQbyuaLKe2WOTlPN7ajpFQ5BP4jViMZFN9Ezlpbn', 'Y1GDDuAxSzNAhh2afzW92WXxt7kZC6q5A4AonAD7rTWpP5Do8ArAmVkoS8v5yLms', '31536000', '2017-02-25 14:33:10', '2017-03-01 22:10:07');

-- ----------------------------
-- Table structure for t_user_id
-- ----------------------------
DROP TABLE IF EXISTS `t_user_id`;
CREATE TABLE `t_user_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user_id
-- ----------------------------
