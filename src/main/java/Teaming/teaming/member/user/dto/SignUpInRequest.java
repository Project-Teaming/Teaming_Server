package Teaming.teaming.member.user.dto;

import Teaming.teaming.member.user.entity.Major;
import jakarta.validation.constraints.NotNull;

public record SignUpInRequest(
		@NotNull String username, // For SignIn
		String name,
		@NotNull String password, // For SignIn
		String email,
		int grade,
		Major mainMajor,
		Major subMajor
) {
}
