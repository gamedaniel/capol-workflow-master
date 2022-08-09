package com.capol.workflow.server.api.instance;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
}
