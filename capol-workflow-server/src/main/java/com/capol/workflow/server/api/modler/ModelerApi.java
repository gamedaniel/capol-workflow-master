package com.capol.workflow.server.api.modler;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TenantEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/flow")
public class ModelerApi {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;



    @GetMapping("/modeldetail")
    public String modelDetail(String processDefKey) throws IOException {
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(getClass().getClassLoader().getResourceAsStream("paiche.bpmn"));

        Collection<FlowNode> flowNodes = modelInstance.getModelElementsByType(FlowNode.class);
        Collection<ExtensionElements> extensionElements = modelInstance.getModelElementsByType(ExtensionElements.class);
        Collection<ServiceTask> serviceTasks = modelInstance.getModelElementsByType(ServiceTask.class);
        Collection<SequenceFlow> sequenceFlows = modelInstance.getModelElementsByType(SequenceFlow.class);
        Collection<Event> events = modelInstance.getModelElementsByType(Event.class);


        System.out.println("---------flow seq---------");
        for (SequenceFlow seqItem :
                sequenceFlows) {

            FlowNode sourceNode = seqItem.getSource();
            FlowNode targetNode = seqItem.getTarget();

            System.out.println(sourceNode == null ? "" : sourceNode.getId());
            System.out.println(" to ");
            System.out.println(targetNode == null ? "" : targetNode.getId());


            ConditionExpression conditionExpression = seqItem.getConditionExpression();
            System.out.println(conditionExpression == null ? "" : conditionExpression.getTextContent());
        }

        System.out.println("---------flow seq---------");

        for (FlowNode flowNode :
                flowNodes) {
            Collection<SequenceFlow> incoming = flowNode.getIncoming();
            Collection<SequenceFlow> outgoing = flowNode.getOutgoing();

            System.out.println(flowNode.getId());
        }

        System.out.println("event id");

        for (Event eventItem : events) {
            System.out.println(eventItem.getId());
            if (eventItem instanceof  StartEvent) {
                StartEvent startEvent = (StartEvent) eventItem;
            } else if (eventItem instanceof  EndEvent) {
                EndEvent endEvent = (EndEvent) eventItem;
            }
        }

        return "success";
    }

    @GetMapping("/queryDefinition")
    public String queryDefinition(String processDefKey) throws IOException {
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> list = definitionQuery
                //查询好比 where
                //.processDefinitionId()
                //.processDefinitionName()
                //.processDefinitionVersion()
                .processDefinitionKey(processDefKey)
                .latestVersion()
                //排序
                .orderByProcessDefinitionVersion().desc()
                //结果
                //.count()
                //.listPage()
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return "Empty";
        }
        StringBuilder builder = new StringBuilder();
        for (ProcessDefinition definition : list) {
            String id = definition.getId();
            String key = definition.getKey();
            int version = definition.getVersion();
            String deploymentId = definition.getDeploymentId();
            String name = definition.getName();
            builder.append("\n\n流程定义id：").append(id)
                    .append("\n流程定义key：").append(key)
                    .append("\n流程定义版本：").append(version)
                    .append("\n流程定义部署id：").append(deploymentId)
                    .append("\n流程定义名称：").append(name);
        }
        String result = builder.toString();
        System.out.println(result);
        return result;
    }

    @GetMapping("/taskinfo")
    public String queryTaskInfo(String taskId) throws IOException {
        String result = null;
        List<IdentityLink> tasks = taskService.getIdentityLinksForTask(taskId);


        return result;
    }

    @PostMapping("/deploy")
    @ResponseBody
    public String deploy(String bpmnPath, String name, String source, String tenantId) {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deploy = deploymentBuilder
                .name(name)
                .source(source)
                .tenantId(tenantId)
                .addClasspathResource(bpmnPath)
                .deploy();
        String result = String.format("部署器\nID:%s\n名称:%s", deploy.getId(), deploy.getName());
        System.out.println(result);
        return result;
    }

    @PutMapping("init/user")
    public String initUser()  throws IOException {
        String result = null;

        TenantEntity tenantEntity1 = new TenantEntity();
        tenantEntity1.setRevision(0);
        tenantEntity1.setId("capol");
        tenantEntity1.setName("华阳国际");
        identityService.saveTenant(tenantEntity1);

        GroupEntity groupEntity1 = new GroupEntity();
        groupEntity1.setId("003plus001");
        groupEntity1.setName("架构部领导");
        groupEntity1.setRevision(0);
        groupEntity1.setType("审批流");
        identityService.saveGroup(groupEntity1);

        GroupEntity groupEntity2 = new GroupEntity();
        groupEntity2.setId("003plus002");
        groupEntity2.setName("架构部职员");
        groupEntity2.setRevision(0);
        groupEntity2.setType("审批流");
        identityService.saveGroup(groupEntity2);

        UserEntity userEntity1=new UserEntity();
        userEntity1.setRevision(0);
        userEntity1.setId("001");
        userEntity1.setFirstName("梁金伟");
        identityService.saveUser(userEntity1);

        UserEntity userEntity2=new UserEntity();
        userEntity2.setRevision(0);
        userEntity2.setId("002");
        userEntity2.setFirstName("万锐");
        identityService.saveUser(userEntity2);

        UserEntity userEntity3=new UserEntity();
        userEntity3.setRevision(0);
        userEntity3.setId("003");
        userEntity3.setFirstName("史聃");
        identityService.saveUser(userEntity3);

        identityService.createMembership("001","003plus001");
        identityService.createMembership("002","003plus001");
        identityService.createMembership("003","003plus002");

        identityService.createTenantGroupMembership("capol", "003plus001");
        identityService.createTenantGroupMembership("capol", "003plus002");


        return result;
    }
}
