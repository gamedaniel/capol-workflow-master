package com.capol.workflow.server.model.dto;

import lombok.Data;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shidan1
 */
@Data
public class FlowPath {
    public FlowPath() {

    }

    public FlowPath(FlowPath flowPath) {
        this.conditions = new ArrayList<>(flowPath.getConditions());

        this.flowPath = new ArrayList<>(flowPath.getFlowPath());
    }


    private List<String> conditions = new ArrayList<>();

    private List<FlowNode> flowPath = new ArrayList<>();
}
