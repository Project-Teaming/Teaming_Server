package Teaming.teaming.member.user.dto;

import Teaming.teaming.member.user.entity.Major;
import Teaming.teaming.member.user.entity.Role;

public record CreateAccessTokenRequest(
		String username,
		Role role
) {
}