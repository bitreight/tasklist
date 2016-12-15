package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.config.security.CustomUserDetails;
import com.bitreight.tasklist.dto.TaskDto;
import com.bitreight.tasklist.service.TaskSearchService;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import com.bitreight.tasklist.util.date.LocalDateEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskSearchController {

    @Autowired
    private TaskSearchService taskSearchService;

    @RequestMapping(value = "tasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskDto>> getAllTasksOfUser(@AuthenticationPrincipal CustomUserDetails user,
                                                           @RequestParam(required = false) String sort)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        List<TaskDto> tasks = taskSearchService.getByUserId(user.getId(), sort);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @RequestMapping(value = "tasks/today", method = RequestMethod.GET)
    public ResponseEntity<List<TaskDto>> getAllTodayTasksOfUser(@AuthenticationPrincipal CustomUserDetails user,
                                                                @RequestParam(required = false) String sort,
                                                                @RequestHeader("Client-Date") LocalDate clientDate)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        List<TaskDto> tasks = taskSearchService
                .getTodayTasksByUserId(user.getId(), clientDate, sort);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @RequestMapping(value = "tasks/week", method = RequestMethod.GET)
    public ResponseEntity<List<TaskDto>> getAllWeekTasksOfUser(@AuthenticationPrincipal CustomUserDetails user,
                                                               @RequestParam(required = false) String sort,
                                                               @RequestHeader("Client-Date") LocalDate clientDate)
            throws ServiceUserNotFoundException, ServiceTaskNotFoundException {

        List<TaskDto> tasks = taskSearchService
                .getWeekTasksByUserId(user.getId(), clientDate, sort);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @RequestMapping(value = "projects/{projectId}/tasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskDto>> getTasksOfProject(@PathVariable int projectId,
                                                           @RequestParam(required = false) String sort)
            throws ServiceTaskNotFoundException, ServiceProjectNotFoundException {

        List<TaskDto> tasks = taskSearchService.getByProjectId(projectId, sort);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
    }
}
