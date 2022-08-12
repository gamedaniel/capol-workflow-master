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

 Date: 11/08/2022 19:12:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_flow_approval_role
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_approval_role`;
CREATE TABLE `t_flow_approval_role`  (
  `id` bigint(20) NOT NULL,
  `enterprise_id` bigint(20) NOT NULL,
  `flow_role_id` bigint(20) NOT NULL,
  `flow_dept_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tabel_index`(`enterprise_id`, `flow_role_id`, `flow_dept_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
