package com.bitreight.tasklist.controller;

import com.bitreight.tasklist.dto.ProjectDto;
import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ResponseEntity<List<ProjectDto>> addProject() {
        return null;
    }

    @RequestMapping(value = "/projects/{project_id}", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDto>> getProject() {
        //principal.
        //List<ProjectDto> projects = projectService.getByUserId(user.getId());
        //return new ResponseEntity<List<ProjectDto>>(projects, HttpStatus.OK);
        return null;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDto>> getUserProjects(@AuthenticationPrincipal UserDto user) {
        List<ProjectDto> projects = projectService.getByUserId(1);
        return new ResponseEntity<List<ProjectDto>>(projects, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{project_id}", method = RequestMethod.PUT)
    public ResponseEntity<List<ProjectDto>> updateProject() {
        return null;
    }

    @RequestMapping(value = "/projects/{project_id}", method = RequestMethod.DELETE)
    public ResponseEntity<List<ProjectDto>> deleteProject() {
        return null;
    }
}
