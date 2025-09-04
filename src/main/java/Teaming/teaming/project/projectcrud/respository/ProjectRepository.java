package Teaming.teaming.project.projectcrud.respository;

import Teaming.teaming.project.projectcrud.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;;import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Optional<Project> findByProjectId(Long projectId);
    Optional<Project> findByProjectManagerName(String projectManagerName);
}
