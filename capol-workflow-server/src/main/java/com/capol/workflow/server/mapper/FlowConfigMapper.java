package com.capol.workflow.server.mapper;

import com.capol.workflow.server.model.po.FlowConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shidan1
 */
@Mapper
public interface FlowConfigMapper {
    void insertFlowConfig(FlowConfig flowConfig);
}
