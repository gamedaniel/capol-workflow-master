/*
 Navicat Premium Data Transfer

 Source Server         : qa-10.1.207.12
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : 10.1.207.12:3306
 Source Schema         : db_workflow2

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 11/08/2022 19:13:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_flow_config_path
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_config_path`;
CREATE TABLE `t_flow_config_path`  (
  `id` int(11) NOT NULL,
  `flow_config_id` int(11) NOT NULL COMMENT '流程配置id',
  `flow_conditions` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路径条件',
  `flow_paths` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程路径',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_flow_config_id`(`flow_config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
