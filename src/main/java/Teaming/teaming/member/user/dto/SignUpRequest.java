package Teaming.teaming.member.user.dto;

import Teaming.teaming.member.user.entity.Major;

public record SignUpRequest(
		String username,
		String name,
		String password,
		String email,
		int grade,
		Major mainMajor,
		Major subMajor
) {
}
