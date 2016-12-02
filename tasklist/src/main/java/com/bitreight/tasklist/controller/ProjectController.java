package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.config.security.CustomUserDetails;
import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.ProjectService;
import com.bitreight.tasklist.service.exception.ServiceProjectAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "projects", method = RequestMethod.POST)
    public ResponseEntity<Integer> createProject(@AuthenticationPrincipal CustomUserDetails user,
                                                 @Valid @RequestBody ProjectDto projectDto)
            throws ServiceProjectAlreadyExistsException, ServiceUserNotFoundException {

        int id = projectService.add(projectDto, user.getId());
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @RequestMapping(value = "projects/{projectId}", method = RequestMethod.GET)
    public ResponseEntity<ProjectDto> getProject(@PathVariable int projectId)
            throws ServiceProjectNotFoundException {

        ProjectDto project = projectService.getById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @RequestMapping(value = "projects", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDto>> getProjectsByUser(@AuthenticationPrincipal CustomUserDetails user)
            throws ServiceUserNotFoundException, ServiceProjectNotFoundException {

        List<ProjectDto> projects = projectService.getByUserId(user.getId());
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @RequestMapping(value = "projects/{projectId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateProject(@PathVariable int projectId,
                                              @Valid @RequestBody ProjectDto projectDto)
            throws ServiceProjectAlreadyExistsException, ServiceProjectNotFoundException {

        projectDto.setId(projectId);
        projectService.update(projectDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "projects/{projectId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProject(@PathVariable int projectId)
            throws ServiceProjectNotFoundException {

        projectService.deleteById(projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}