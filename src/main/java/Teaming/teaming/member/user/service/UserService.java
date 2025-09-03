package Teaming.teaming.member.user.service;

import Teaming.teaming.member.user.dto.CreateAccessTokenRequest;
import Teaming.teaming.member.user.dto.SignUpInRequest;
import Teaming.teaming.member.user.entity.Major;
import Teaming.teaming.member.user.entity.Role;
import Teaming.teaming.member.user.entity.User;
import Teaming.teaming.member.user.jwt.JwtProvider;
import Teaming.teaming.member.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	// 회원가입
	public ResponseEntity<?> signUp(SignUpInRequest request) {
		// 사용자명 중북 체크
		if (userRepository.existsByUsername(request.username())) {
			return ResponseEntity.badRequest().body(Map.of("username_error", "중복된 사용자명 입니다."));
		}

		// 이메일 중복 체크
		if (userRepository.existsByEmail(request.email())) {
			return ResponseEntity.badRequest().body(Map.of("email_error", "해당 이메일로 이미 가입된 계정이 있습니다."));
		}

		// 비밀번호 검증식 통과 여부
		String rawPassword = request.password();
		if (!isValidPassword(rawPassword)) {
			return ResponseEntity.badRequest().body(Map.of("password_error", "비밀번호에는 영문 대소문자 중 하나, 숫자, 특수문자가 포함된 8글자 이상의 문자열이여야 합니다."));
		}

		// 부전공이 없는 경우에는 Null값 대신에 NONE으로 들어가게
		Major subMajor = request.subMajor();
		if (request.subMajor().equals(request.mainMajor())) {
			subMajor = Major.NONE;
		}

		User user = User.builder()
				.username(request.username())
				.name(request.name())
				.password(passwordEncoder.encode(rawPassword))
				.email(request.email())
				.grade(request.grade())
				.mainMajor(request.mainMajor())
				.subMajor(subMajor)
				.role(Role.ROLE_STUDENT) // 학생 계정은 ROLE_STUDENT, 관리자 계정은 ROLE_ADMIN, 선생님 계정은 ROLE_TEACHER
				.build();
		userRepository.save(user);
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<?> signIn(SignUpInRequest request) {
		User user = userRepository.findByUsername(request.username()).orElseThrow(()
				-> new IllegalArgumentException("사용자명 혹은 비밀번호가 잘못되었습니다."));
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new IllegalArgumentException("사용자명 혹은 비밀번호가 잘못되었습니다.");
		}

		CreateAccessTokenRequest accessTokenRequest = new CreateAccessTokenRequest(
				user.getUsername(),
				user.getRole()
		);

		// 리프레시 토큰 발급(예정)

		String accessToken = jwtProvider.createAccessToken(accessTokenRequest);
		return ResponseEntity.ok().body(Map.of("accessToken", accessToken));
	}


/** 검증식 및 무결성 체크 로직들 **/
	// 비밀번호 검증식
	private boolean isValidPassword(String password) {
		if (password == null || password.length() < 8) { // 만약  비밀번호가 공백이거나 8자 미만인지
			return false;
		}
		boolean hasLetter = password.matches(".*[a-zA-Z].*"); // 영어 대소문자중 하나라도 포함되어 있는지
		boolean hasDigit = password.matches(".*\\d.*"); // 숫자가 하나라도 있는지
		boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"); // 특수문자가 하나라도 있는지
		return hasLetter && hasDigit && hasSpecial;
	}
}
