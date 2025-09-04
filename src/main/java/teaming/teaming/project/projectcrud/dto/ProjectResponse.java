package teaming.teaming.project.projectcrud.dto;

import java.util.List;

public record ProjectResponse(
        String ProjectName,
        String content,
        String projectManagerName,
        List<String> usernames

) {
}
