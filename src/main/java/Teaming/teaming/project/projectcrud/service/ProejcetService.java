package Teaming.teaming.project.projectcrud.service;

import Teaming.teaming.member.user.entity.Role;
import Teaming.teaming.member.user.entity.User;
import Teaming.teaming.member.user.repository.UserRepository;
import Teaming.teaming.project.exception.ProjectNotFoundException;
import Teaming.teaming.project.projectcrud.dto.CreateRequest;
import Teaming.teaming.project.projectcrud.dto.ProjectResponse;
import Teaming.teaming.project.projectcrud.entity.Project;
import Teaming.teaming.project.projectcrud.respository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Port;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProejcetService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ResponseEntity<String> CreateProject(CreateRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        if (user.getRole() == Role.ROLE_STUDENT) {
            Project project = Project.builder()
                    .projectName(request.ProjectName())
                    .content(request.content())
                    .projectManagerName(auth.getName())
                    .user(List.of(user))
                    .build();
                projectRepository.save(project);
        }
            return ResponseEntity.ok(request.ProjectName());
    }

    public List<ProjectResponse> getProjectList() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(projcet -> new ProjectResponse(
                projcet.getProjectName(),
                projcet.getContent(),
                projcet.getProjectManagerName(),
                projcet.getUser().stream().map(User::getUsername).toList()
        ))
                .toList();
    }

    public ProjectResponse UpdateProjcet(CreateRequest request, Long id) {
        Project project = projectRepository.findByProjectId(id)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));
        project.setProjectName(request.ProjectName());
        project.setContent(request.content());
        projectRepository.save(project);
        return new ProjectResponse(
                project.getProjectName(),
                project.getContent(),
                project.getProjectManagerName(),
                project.getUser().stream().map(User::getUsername).toList()
        );
    }

    public ResponseEntity<?> DeleteProjcet(Long id) {
        Project project = projectRepository.findByProjectId(id)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다"));
        projectRepository.delete(project);
        return ResponseEntity.ok(Map.of("Message", "프로젝트가 제거되었습니다."));
    }
}
