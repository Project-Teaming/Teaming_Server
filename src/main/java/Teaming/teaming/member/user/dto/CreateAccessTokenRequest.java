package teaming.teaming.member.user.dto;

import teaming.teaming.member.user.entity.Role;

public record CreateAccessTokenRequest(
		String name,
		String email,
		Role role
) {}