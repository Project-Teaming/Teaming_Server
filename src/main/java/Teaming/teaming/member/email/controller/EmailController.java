package teaming.teaming.member.email.controller;

import teaming.teaming.member.email.dto.BaseResponse;
import teaming.teaming.member.email.dto.EmailCheckRequest;
import teaming.teaming.member.email.dto.EmailSendRequest;
import teaming.teaming.member.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Tag(name = "이메일 인증", description = "이메일 발송 및 인증 API")
public class EmailController {
	private final EmailService emailService;

	@PostMapping("/send")
	@Operation(summary = "인증코드 발송")
	public BaseResponse sendEmail(@RequestBody EmailSendRequest request) {
		emailService.sendEmail(request.email());
		return BaseResponse.ok("이메일을 성공적으로 발송하였습니다.");
	}

	@PostMapping("/verify")
	@Operation(summary = "이메일 인증 코드 확인")
	public BaseResponse checkEmail(@RequestBody EmailCheckRequest request) {
		return emailService.chekAuthNum(request.email(), request.authNum());
	}
}
