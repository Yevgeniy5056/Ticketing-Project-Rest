package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProjects() {

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Projects successfully retrieved")
                .data(projectService.listAllProjects()).build());
    }

    @GetMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("projectCode") String projectCode) {

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project successfully retrieved")
                .data(projectService.getByProjectCode(projectCode)).build());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO project) {

        projectService.save(project);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("Project successfully created").build());
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO project) {

        projectService.update(project);

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project successfully updated").build());
    }

    @DeleteMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode") String projectCode) {

        projectService.delete(projectCode);

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project successfully deleted").build());
    }

    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectByManager() {

        projectService.listAllProjectDetails();

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project successfully retrieved").build());
    }

    @PutMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String projectCode) {

        projectService.complete(projectCode);

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Project successfully completed").build());
    }
}
