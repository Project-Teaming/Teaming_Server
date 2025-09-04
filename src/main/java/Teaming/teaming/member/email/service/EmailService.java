package Teaming.teaming.member.email.service;

import Teaming.teaming.member.email.config.RedisConfig;
import Teaming.teaming.member.email.dto.BaseResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	private final JavaMailSender mailSender;
	private final RedisConfig redisConfig;
	private int authNumber;

	@Value("${spring.mail.username}") private String serviceName;

	public void makeRandomNum() {
		authNumber = 100000 + new Random().nextInt(899999); // 인증번호는 6자리
	}

	public void sendEmail(String email) {
		makeRandomNum();
		String title = "Teaming 회원가입 용 본인인증 코드입니다.";
		String contentTemplate = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Teaming 인증코드</title>
    </head>
    <body style="margin: 0; padding: 0; background-color: #000000; font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif;">
        <div style="min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 40px; background-color: #000000;">
            <div style="background: #000000; border: 1px solid #1a1a1a; border-radius: 16px; max-width: 600px; margin: 0 auto; width: 100%; overflow: hidden; box-shadow: 0 25px 50px rgba(0,0,0,0.5);">
                <!-- 헤더 섹션 -->
                <div style="padding: 64px 48px 40px; text-align: center; border-bottom: 1px solid #1a1a1a;">
                    <h1 style="color: #ffffff; font-size: 40px; font-weight: 800; margin: 0 0 16px; letter-spacing: -1px;">
                        <span style="color: #3E80F4;">T</span>eaming
                    </h1>
                    <p style="color: #ffffff; font-size: 18px; font-weight: 700; margin: 0; line-height: 1.4;">
                        프로젝트를 <span style="color: #3E80F4;">더</span> 쉽게,<br>
                        협업을 <span style="color: #3E80F4;">더</span> 쉽게
                    </p>
                </div>
                <!-- 본문 섹션 -->
                <div style="padding: 56px 48px; text-align: center;">
                    <p style="color: #ffffff; font-weight: 700; font-size: 16px; margin: 0 0 32px; line-height: 1.5;">
                        저희 <span style="color: #3E80F4;">T</span>eaming 서비스를 이용해 주셔서 감사합니다.
                    </p>

                    <p style="color: #ffffff; font-weight: 700; font-size: 16px; margin: 0 0 8px;">
                        아래 인증코드를 회원가입 란에 정확히 입력해주세요.
                    </p>
                    <p style="color: #888888; font-size: 13px; margin: 0 0 32px; font-weight: 400;">
                        코드는 5분동안 유효합니다.
                    </p>

                    <!-- 인증코드 박스 -->
                    <div style="margin: 0 0 40px;">
                        <div style="display: inline-block; background: linear-gradient(135deg, #3E80F4 0%, #5B9BF8 100%); padding: 20px 48px; border-radius: 16px; box-shadow: 0 8px 24px rgba(62, 128, 244, 0.3); position: relative; overflow: hidden;">
                            <!-- 상단 하이라이트 -->
                            <span style="color: #ffffff; font-size: 36px; font-weight: 800; letter-spacing: 3px; display: inline-block; text-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                                {{AUTH_CODE}}
                            </span>
                        </div>
                    </div>
                    <!-- 안내사항 -->
                    <div style="background: #111111; border: 1px solid #1a1a1a; border-radius: 12px; padding: 24px; text-align: left;">
                        <div style="display: flex; align-items: center; margin-bottom: 16px;">
                            <span style="color: #ffffff; font-weight: 600; font-size: 14px;">주의사항</span>
                        </div>
                        <ul style="color: #cccccc; font-size: 13px; margin: 0; padding-left: 20px; line-height: 1.6; font-weight: 400;">
                            <li style="margin-bottom: 6px;">인증코드는 타인과 공유하지 마세요</li>
                            <li style="margin-bottom: 6px;">5분이 지나면 새로운 인증코드를 발급받아야 합니다</li>
                            <li style="margin-bottom: 0;">본인이 요청하지 않은 인증이라면 무시하세요</li>
                        </ul>
                    </div>
                </div>
                <!-- 푸터 -->
                <div style="background: #111111; padding: 32px 48px; text-align: center; border-top: 1px solid #1a1a1a;">
                    <p style="color: #888888; font-size: 14px; margin: 0 0 8px; font-weight: 400;">
                        이 이메일은 Teaming 서비스 가입을 위해 발송되었습니다
                    </p>
                    <p style="color: #666666; font-size: 13px; margin: 0; font-weight: 400;">
                        © 2025 Teaming. All rights reserved.
                    </p>
                </div>
            </div>
        </div>
    </body>
    </html>
    """;

		String content = contentTemplate.replace("{{AUTH_CODE}}", String.valueOf(authNumber));

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(serviceName);
			helper.setTo(email);
			helper.setSubject(title);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			logger.error("인증코드를 정상적으로 발송하지 못했습니다.", e);
		}
		// Redis에 5분간 저장
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		valueOperations.set(email, Integer.toString(authNumber), 5, TimeUnit.MINUTES);
	}
	// 인증코드 확인
	public BaseResponse chekAuthNum(String email, String authNum) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String code =  valueOperations.get(email);

		if (Objects.equals(code, authNum)) {
			redisConfig.redisTemplate().delete(email);
			redisConfig.redisTemplate().delete(authNum);
			return BaseResponse.ok("이메일이 확인되었습니다.");
		} else {
			return BaseResponse.of(HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다.");
		}
	}
}
