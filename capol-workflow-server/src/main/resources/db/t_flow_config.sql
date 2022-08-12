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

 Date: 11/08/2022 19:13:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_flow_config
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_config`;
CREATE TABLE `t_flow_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `enterprise_id` bigint(20) NOT NULL COMMENT '企业id',
  `flow_type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '平台流程类型 1-平台， 2-企业',
  `flow_config_type` tinyint(4) NOT NULL COMMENT '流程配置类型',
  `flow_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
  `flow_comments` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程说明',
  `flow_sence` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程场景',
  `flow_config_apply` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程',
  `flow_deploy_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程引擎部署key',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '流程状态 0-草稿 1-启用 2-禁用',
  `creator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `creator_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `created_host_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人ip',
  `last_operator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人',
  `last_operator_id` bigint(20) NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `update_host_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人ip',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_enterprise_id`(`enterprise_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
