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

 Date: 11/08/2022 19:13:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_flow_inst_map
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_inst_map`;
CREATE TABLE `t_flow_inst_map`  (
  `id` int(11) NOT NULL,
  `enterprise_id` int(11) NOT NULL COMMENT '企业id',
  `flow_applicant` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请用户id',
  `flow_applicant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请用户名字',
  `flow_inst_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
  `flow_apply_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
  `flow_apply_comments` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程摘要',
  `flow_curr_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前处理人',
  `flow_curr_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '当前状态  0 处理中 1 完成  2 驳回  3 停办 4 逾期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_enterprise_id`(`enterprise_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
