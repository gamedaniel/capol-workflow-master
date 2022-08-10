package com.capol.workflow.server.model.base;

import java.util.Date;

/**
 * @author shidan1
 */
public class BaseModel {
    /**
     * 创建人
     * */
    private String creator;

    /**
     * 创建人id
     * */
    private Long creatorId;

    /**
     * 创建时间
     * */
    private Date createTime;

    /**
     * 创建人ip
     * */
    private String createdHostIp;

    /**
     * 修改人
     * */
    private String lastOperator;

    /**
     * 修改人id
     * */
    private Long lastOperatorId;

    /**
     * 修改时间
     * */
    private Date updateTime;

    /**
     * 修改人ip
     * */
    private String updateHostIp;
}
