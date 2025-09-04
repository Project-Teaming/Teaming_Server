package teaming.teaming.member.email.dto;

public record EmailCheckRequest(
		String email,
		String authNum
) {
}
