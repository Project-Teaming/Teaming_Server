package Teaming.teaming.member.user.dto;

import Teaming.teaming.member.user.entity.Major;

public record CreateAccessTokenRequest(
		String username,
		String name,
		String email,
		int grade,
		Major mainMajor,
		Major subMajor
) {
}