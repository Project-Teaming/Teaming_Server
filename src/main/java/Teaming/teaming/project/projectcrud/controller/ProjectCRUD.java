package teaming.teaming.project.projectcrud.controller;

import teaming.teaming.project.projectcrud.dto.CreateRequest;
import teaming.teaming.project.projectcrud.dto.ProjectResponse;
import teaming.teaming.project.projectcrud.service.ProejcetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectCRUD {

    private final ProejcetService projcetService;

    @PostMapping("/create")
    public ResponseEntity<String> CreateProject(@RequestBody CreateRequest request) {
        return projcetService.CreateProject(request);
    }

    @GetMapping("/read")
    public List<ProjectResponse> readProject() {
        return projcetService.getProjectList();
    }

    @PatchMapping("/update/{id}")
    public ProjectResponse updateProject(@RequestBody CreateRequest request, @PathVariable Long id) {
        return projcetService.UpdateProjcet(request,id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        return projcetService.DeleteProjcet(id);
    }
}
