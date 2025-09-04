package Teaming.teaming.member.user.controller;

import Teaming.teaming.member.user.dto.SignInRequest;
import Teaming.teaming.member.user.dto.SignUpRequest;
import Teaming.teaming.member.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
	private final UserService userService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
		return userService.signUp(request);
	}

	// 로그인
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
		return userService.signIn(request);
	}
}
