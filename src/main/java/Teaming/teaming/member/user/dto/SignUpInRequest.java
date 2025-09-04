package teaming.teaming.member.user.dto;

import teaming.teaming.member.user.entity.Major;
import jakarta.validation.constraints.NotNull;

public record SignUpInRequest(
		String username,
		String name,
		@NotNull
		String password, // for SignIn
		@NotNull
		String email, // for SignIn
		int grade,
		Major mainMajor,
		Major subMajor
) {
}
