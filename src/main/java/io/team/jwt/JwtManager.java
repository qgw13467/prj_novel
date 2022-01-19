package io.team.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.team.domain.User;

import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtManager {
	private final String securityKey = "q12095uwaet093q4u5-Q9UWR0T82Q4YT0QE8TH29802Q3UWR-9TQA2T803YHRF-	9Q30YT38"; // TODO 민감정보는 따로 분리하는 것이 좋다
	private final Long expiredTime = 1000 * 60L * 60L * 3L; // 유효시간 3시간

	public String generateJwtToken(User newUser) {
		Date now = new Date();
		return Jwts.builder().setSubject(newUser.getMem_userid()) // 보통 username
				.setHeader(createHeader()).setClaims(createClaims(newUser)) // 클레임, 토큰에 포함될 정보
				.setExpiration(new Date(now.getTime() + expiredTime)) // 만료일
				.signWith(SignatureAlgorithm.HS256, securityKey).compact();
	}

	private Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		header.put("alg", "HS256"); // 해시 256 사용하여 암호화
		header.put("regDate", System.currentTimeMillis());
		return header;
	}

	private Map<String, Object> createClaims(User newUser) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", newUser.getMem_userid()); // username
		claims.put("roles", newUser.getState()); // 인가정보
		return claims;
	}

	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();
	}

	public String getUsernameFromToken(String token) {
		return (String) getClaims(token).get("username");
	}

}
