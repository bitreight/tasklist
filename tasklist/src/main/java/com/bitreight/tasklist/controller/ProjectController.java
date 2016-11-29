package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.config.security.CustomUserDetails;
import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.service.ProjectService;
import com.bitreight.tasklist.service.exception.ServiceProjectAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createProject(@AuthenticationPrincipal CustomUserDetails user,
                                                          @RequestBody ProjectDto projectDto)
            throws ServiceProjectAlreadyExistsException {

        projectService.add(projectDto, user.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);    //need id
    }

    @RequestMapping(value = "{projectId}", method = RequestMethod.GET)
    public ResponseEntity<ProjectDto> getProject(@PathVariable int projectId) {

        ProjectDto project = projectService.getById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDto>> getProjectsByUser(@AuthenticationPrincipal CustomUserDetails user) {

        List<ProjectDto> projects = projectService.getByUserId(user.getId());
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @RequestMapping(value = "{projectId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateProject(@PathVariable int projectId,
                                              @RequestBody ProjectDto projectDto)
            throws ServiceProjectAlreadyExistsException {

        projectDto.setId(projectId);
        projectService.update(projectDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //return updated
    }

    @RequestMapping(value = "{projectId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProject(@PathVariable int projectId) {

        projectService.deleteById(projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
