package teaming.teaming.member.user.controller;

import teaming.teaming.member.user.dto.SignUpInRequest;
import teaming.teaming.member.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class UserController {
	private final UserService userService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpInRequest request) {
		return userService.signUp(request);
	}

	// 로그인
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody SignUpInRequest request) {
		log.info("이메일은 다음과 같습니다." + request.email());
		return userService.signIn(request);
	}
}
