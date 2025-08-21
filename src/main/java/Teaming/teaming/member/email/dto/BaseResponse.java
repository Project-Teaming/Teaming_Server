package Teaming.teaming.member.email.dto;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseResponse {
	private boolean success;
	private String message;
	private int status;

	// 성공 응답
	public static BaseResponse ok(String message) {
		return BaseResponse.builder()
				.success(true)
				.message(message)
				.status(HttpStatus.OK.value())
				.build();
	}

	// 실패 응답
	public static <T> BaseResponse of(HttpStatus status, String message) {
		return BaseResponse.<T> builder()
				.success(false)
				.message(message)
				.status(status.value())
				.build();
	}
}
