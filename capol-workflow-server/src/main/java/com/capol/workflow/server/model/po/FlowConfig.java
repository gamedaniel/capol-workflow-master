package com.capol.workflow.server.model.po;

import com.capol.workflow.server.model.base.BaseModel;
import lombok.Data;

/**
 * @author shidan1
 */
@Data
public class FlowConfig extends BaseModel {
    /**
     * 主键
     * */
    private Long id;

    /**
     * 企业id
     * */
    private Long enterpriseId;

    /**
     * 平台流程类型 1-平台， 2-企业
     * */
    private short flowType;

    /**
     * 流程类型
     * */
    private short flowConfigType;

    /**
     * 流程名称
     * */
    private String flowName;

    /**
     * 流程说明
     * */
    private String flowComments;

    /**
     * 流程场景
     * */
    private String flowSence;

    /**
     * 流程应用场景？
     * */
    private String flowConfigApply;

    /**
     * 流程引擎部署key
     * */
    private String flowDeployKey;

    /**
     * 流程状态 0-草稿 1-启用 2-禁用
     * */
    private short status;
}
