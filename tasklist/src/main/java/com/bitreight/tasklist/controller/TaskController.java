package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.TaskService;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskVersionIsOutdatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "projects/{projectId}/tasks", method = RequestMethod.POST)
    public ResponseEntity<Integer> createTask(@PathVariable int projectId,
                                              @Valid @RequestBody TaskDto taskDto)
            throws ServiceTaskAlreadyExistsException, ServiceProjectNotFoundException {

        int newTaskId = taskService.add(taskDto, projectId);
        return new ResponseEntity<>(newTaskId, HttpStatus.CREATED);
    }

    @RequestMapping(value = "tasks/{taskId}", method = RequestMethod.GET)
    public ResponseEntity<TaskDto> getTask(@PathVariable int taskId)
            throws ServiceTaskNotFoundException {

        TaskDto task = taskService.getById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @RequestMapping(value = "tasks/{taskId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateTask(@PathVariable int taskId,
                                           @Valid @RequestBody TaskDto taskDto)
            throws ServiceTaskVersionIsOutdatedException, ServiceTaskAlreadyExistsException, ServiceTaskNotFoundException {

        taskDto.setId(taskId);
        taskService.update(taskDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "tasks/{taskId}", method = RequestMethod.DELETE)
    public ResponseEntity<TaskDto> deleteTask(@PathVariable int taskId)
            throws ServiceTaskNotFoundException {

        taskService.deleteById(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "tasks/{taskId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> setTaskCompleted(@PathVariable int taskId,
                                                 @RequestParam(value = "is_completed") boolean isCompleted)
            throws ServiceTaskNotFoundException {

        taskService.setIsCompleted(taskId, isCompleted);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}