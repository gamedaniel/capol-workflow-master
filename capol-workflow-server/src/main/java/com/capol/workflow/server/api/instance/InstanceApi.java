package com.capol.workflow.server.api.instance;

import com.capol.workflow.server.model.dto.FlowPath;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/instance")
public class InstanceApi {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @PutMapping("/start")
    public String startInstance() {

        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey("paiche").tenantIdIn("capol").latestVersion().singleResult();

            /**
             * dept_leader
             * auto_dept_leader
             * auto_manager
             * */
            //identityService.setAuthenticatedUserId("003");
            String processDefinitionId = processDefinition.getId();
            UUID uuid = UUID.randomUUID();
            String businessKey = uuid.toString().replace("-", "");
            Map<String, Object> map = new HashMap<>();
            map.put("dept_leader", "004");
            map.put("auto_dept_leader", "002");
            map.put("auto_manager", "003");
            map.put("role", "002");
            map.put("startUserId", "003");

            //以businessKey传参方式：启动流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey, map);
        } catch (Exception ex) {
            System.out.println("error : " + ex.toString());
        } finally {

        }

        return "successed";
    }

    @PutMapping("/task")
    public String task() {

        List<Task> taskList = taskService
                .createTaskQuery()
                .taskCandidateUser("001")
                .list();

        for (Task task : taskList) {
            System.out.println("#############");
            System.out.println(task.getId());
            System.out.println(task.getCreateTime());
            System.out.println(task.getPriority());
            System.out.println(task.getExecutionId());
            System.out.println("#############");

            Map<String, Object> map = new HashMap<>();
            map.put("approval_2", "1");
            taskService.complete(task.getId(), map);
        }

        List<Task> taskList1 = taskService
                .createTaskQuery()
                .taskCandidateGroup("003plus001")
                .list();

        for (Task task : taskList1) {
            System.out.println("#############");
            System.out.println(task.getId());
            System.out.println(task.getCreateTime());
            System.out.println(task.getPriority());
            System.out.println(task.getExecutionId());
            System.out.println("#############");
        }

        return "successed";
    }

    @GetMapping("/flow")
    public String instanceFlow() {
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(getClass().getClassLoader().getResourceAsStream("paiche.bpmn"));

        Collection<FlowNode> flowNodes = modelInstance.getModelElementsByType(FlowNode.class);
        Collection<ExtensionElements> extensionElements = modelInstance.getModelElementsByType(ExtensionElements.class);
        Collection<ServiceTask> serviceTasks = modelInstance.getModelElementsByType(ServiceTask.class);
        Collection<SequenceFlow> sequenceFlows = modelInstance.getModelElementsByType(SequenceFlow.class);
        Collection<Event> events = modelInstance.getModelElementsByType(Event.class);

        StartEvent startEvent = null;
        List<EndEvent> endEvents = new ArrayList<>();
        for (Event eventItem : events) {
            System.out.println(eventItem.getId());
            if (eventItem instanceof  StartEvent) {
                startEvent = (StartEvent) eventItem;
            } else if (eventItem instanceof  EndEvent) {
                EndEvent endEvent = (EndEvent) eventItem;
                endEvents.add(endEvent);
            }
        }

        if (startEvent == null) {
            return "failed";
        }

        List<FlowPath> paths = new ArrayList<>();
        FlowPath initPaths =  new FlowPath();
        FlowNode statrNode = flowNodes.stream().filter(o -> "startEvent".equals(o.getElementType().getTypeName())).findFirst().orElse(null);

        initPaths.getFlowPath().add(statrNode);
        calcFlowPath(statrNode, initPaths, paths);
        for (FlowPath item :
                paths) {
            String conditionKey = "";
            for (String condition :
                    item.getConditions()) {
                conditionKey += condition;
            }

            String flowString = "";
            for (FlowNode flowItem :
                    item.getFlowPath()) {
                flowString += flowItem.getId() + " to ";
            }
            System.out.println(conditionKey);
            System.out.println(flowString);
            System.out.println("---------FlowNode---------");
        }

//        System.out.println("---------FlowNode seq---------");
//        for (FlowNode flowNode :
//                flowNodes) {
//            Collection<SequenceFlow> incoming = flowNode.getIncoming();
//            Collection<SequenceFlow> outgoing = flowNode.getOutgoing();
//
//            System.out.println(flowNode.getElementType());
//        }
//
//        System.out.println("---------FlowNode seq---------");

//        System.out.println("---------flow seq---------");
//        for (SequenceFlow seqItem :
//                sequenceFlows) {
//            FlowNode sourceNode = seqItem.getSource();
//            FlowNode targetNode = seqItem.getTarget();
//
//            if (sourceNode.getId().equals(statrNode.getId())) {
//                System.out.println("aaaaaaa");
//            }
//
//            System.out.println(sourceNode.getElementType().getTypeName());
//            System.out.println(targetNode.getElementType().getTypeName());
//
//            System.out.println(sourceNode == null ? "" : sourceNode.getId());
//            System.out.println(" to ");
//            System.out.println(targetNode == null ? "" : targetNode.getId());
//
//
//            ConditionExpression conditionExpression = seqItem.getConditionExpression();
//            System.out.println(conditionExpression == null ? "" : conditionExpression.getTextContent());
//        }
//        System.out.println("---------flow seq---------");

        return "succeed";
    }

    private void calcFlowPath(FlowNode flowNode, FlowPath previewFlowPath, List<FlowPath> flowPaths) {
        Collection<SequenceFlow> outgoing = flowNode.getOutgoing();

        int outSize = outgoing == null ? 0 : outgoing.size();

        if (outSize == 0) {
            flowPaths.add(previewFlowPath);
            return;
        }


        for (SequenceFlow sequenceFlow : outgoing) {
            FlowPath thisFlowPath = new FlowPath(previewFlowPath);
            ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();
            FlowNode nextFlowNode = sequenceFlow.getTarget();
            if (thisFlowPath.getFlowPath().stream().anyMatch(o -> o.getId().equals(nextFlowNode.getId()))) {
                continue;
            }

            if (conditionExpression != null) {
                thisFlowPath.getConditions().add(conditionExpression.getTextContent());
            }

            thisFlowPath.getFlowPath().add(nextFlowNode);
            if (nextFlowNode != null) {
                calcFlowPath(nextFlowNode, thisFlowPath, flowPaths);
            }

        }

    }
}
