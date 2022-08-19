package com.capol.workflow.server.api.modler;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.Expression;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.*;
import org.camunda.bpm.engine.impl.pvm.runtime.ExecutionImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flow")
public class ModelerApi {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;


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

        //List<Deployment> deploymentEntities = repositoryService.createDeploymentQuery().list();
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

    @GetMapping("test/preview")
    public String preview()  throws IOException {
        ExpressionFactory factory = new ExpressionFactoryImpl();

        SimpleContext context = new SimpleContext(new SimpleResolver());
        context.setVariable("role", factory.createValueExpression("001", Object.class));
        context.setVariable("approval_2", factory.createValueExpression("0", Object.class));
        ValueExpression expression = factory.createValueExpression(context, "${role==\"001\" and approval_2==0}", Boolean.class);
        Object value = expression.getValue(context);
        System.out.println(value);
        return "preview";
    }

    @GetMapping("test/preview1")
    public String preview1()  throws IOException {
        List<Deployment> deploymentEntities = repositoryService.createDeploymentQuery()
                .deploymentId("capol-workflow-server-a05ac541-1ddb-11ed-8f8f-a08cfddabf91")
                .list();
        Deployment item = deploymentEntities.get(0);

        InputStream ioStream = repositoryService.getResourceAsStream(item.getId(), "paiche.bpmn");
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(ioStream);
        Collection<FlowNode> flowNodes = modelInstance.getModelElementsByType(FlowNode.class);

        for (FlowNode flowNode :
                flowNodes) {
            if (flowNode.getId().equals("usertask2")) {
                Collection<CamundaProperties> camundaPropertiesCollection = flowNode.getExtensionElements().getChildElementsByType(CamundaProperties.class);
                for (CamundaProperties camundaProperties : camundaPropertiesCollection) {
                    for (CamundaProperty camundaProperty : camundaProperties.getCamundaProperties()) {
                        System.out.println(camundaProperty.getCamundaName());
                        System.out.println(camundaProperty.getCamundaValue());
                    }

                }
                //System.out.println(flowNode.getId());
            }

            //System.out.println(flowNode.getId());

        }

        Collection<ExtensionElements> extensionElements = modelInstance.getModelElementsByType(ExtensionElements.class);

        return "preview1";
    }


    @GetMapping("user/{userId}")
    public List<User> preview1(@PathVariable String userId)  throws IOException {
        List<User> users = identityService.createUserQuery().userId(userId).list();

        for (User userEntity: users) {
            System.out.println(userEntity.getId());
        }

        return users;
    }

    @GetMapping("task/can")
    public String taskCan()  throws IOException {
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().taskAssignee("004").list();

        return "preview1";
    }
}
