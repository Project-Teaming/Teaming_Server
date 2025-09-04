package teaming.teaming.member.user.jwt;

import teaming.teaming.member.user.dto.CreateAccessTokenRequest;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
	private final SecretKey key;
	private final long Validity = 3600000; // 1시간

	public JwtProvider() {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.load();

		String secret = dotenv.get("JWT_SECRET");

		if (secret == null || secret.isEmpty()) {
			throw new IllegalStateException("JWT_SECRET의 무결성을 확인할 수 없습니다.");
		}
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	// 엑세스 토큰 발급
	public String createAccessToken(CreateAccessTokenRequest request) {
		Date now = new Date();
		Date accessTokenExp = new Date(now.getTime() + Validity);

		return Jwts
				.builder()
				.subject(request.name())

				.claim("email", request.email())
				.claim("role", request.role())
				.claim("tokenType", "access_token")

				.issuedAt(now)
				.expiration(accessTokenExp)
				.signWith(key)
				.compact();
	}

	// 토큰에서 권한 끄집어 내기
	public String getRole(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		return claims.get("role", String.class);
	}

	// 토큰에서 토큰 타입(access_token, refresh_toke)끄집어냄
	public String getTokenType(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		return claims.get("tokenType").toString();
	}

	public String getEmail(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		return claims.get("email", String.class);
	}

	// 토큰 무결성 검사
	public boolean validateToken(String token) {
		try {
			Jws<Claims> jwsClaims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token);

			Claims claims = jwsClaims.getPayload();
			Date exp = claims.getExpiration();

			// 토큰 만료
			if (exp.before(new Date())) {
				return false;
			}

			// 토큰의 타입이 access_token이거나 refresh_token인 경우 유효한 토큰
			String tokenType = claims.get("tokenType", String.class);
			if (tokenType.equals("access_token")) {
				return true;
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			return false;
		}
		return false;
	}

	public String resolveToken(HttpServletRequest request) {
		String jwt = request.getHeader("Authorization");
		if(jwt != null && jwt.startsWith("Bearer ")) {
			return jwt.substring(7);
		}
		return null;
	}
}
