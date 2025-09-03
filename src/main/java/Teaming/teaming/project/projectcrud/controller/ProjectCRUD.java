package Teaming.teaming.project.projectcrud.controller;

import Teaming.teaming.project.projectcrud.dto.CreateRequest;
import Teaming.teaming.project.projectcrud.service.ProejcetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectCRUD {

    private final ProejcetService proejcetService;

    @PostMapping("/create")
    public ResponseEntity<String> CreateProject(@RequestBody CreateRequest request) {
        return proejcetService.CreateProject(request);
    }
}
