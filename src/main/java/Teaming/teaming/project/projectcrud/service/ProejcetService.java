package Teaming.teaming.project.projectcrud.service;

import Teaming.teaming.member.user.entity.Role;
import Teaming.teaming.member.user.entity.User;
import Teaming.teaming.member.user.repository.UserRepository;
import Teaming.teaming.project.exception.ProjectNotFoundException;
import Teaming.teaming.project.projectcrud.dto.CreateRequest;
import Teaming.teaming.project.projectcrud.entity.Project;
import Teaming.teaming.project.projectcrud.respository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
                    .ProjectManagerName(auth.getName())
                    .user(List.of(user))
                    .build();
                projectRepository.save(project);
        }
            return ResponseEntity.ok("프로젝트가 생성되었습니다.");
    }

    public User addUser(Long projectId, User user) {

        Project project = projectRepository.findByProjectId(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        project.getUser().add(user);

        return userRepository.save(user);
    }
}
