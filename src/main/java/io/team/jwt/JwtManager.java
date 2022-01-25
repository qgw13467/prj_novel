package io.team.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.team.domain.User;

import org.apache.ibatis.ognl.ClassCacheInspector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Service
public class JwtManager {

	private String securityKey = "sadf1023894r2039hreiwo1309rhi1-2934ieu2130i2tehf123890h"; // TODO 민감정보는 따로 분리하는 것이 좋다

	private final Long expiredTime = 1000 * 60 * 20L;

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
		claims.put("mem_id", newUser.getMem_id());
		claims.put("mem_userid", newUser.getMem_userid()); // username
		claims.put("mem_email", newUser.getMem_email());
		claims.put("mem_nick", newUser.getMem_nick());// 인가정보
		return claims;
	}

	public Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();
	}

	public int getIdFromToken(String token) {
		return (int) getClaims(token).get("mem_id");
	}

	public String getUserIdFromToken(String token) {
		return (String) getClaims(token).get("mem_userid");
	}

	public String getEmailFromToken(String token) {
		return (String) getClaims(token).get("mem_email");
	}
	public String getNickFromToken(String token) {
		return (String) getClaims(token).get("mem_nick");
	}

}
