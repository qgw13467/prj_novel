package io.team.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.team.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Service
public class JwtManager {
	
	@Value("${JWT.SECRET}")
	private String securityKey; // TODO 민감정보는 따로 분리하는 것이 좋다
	
 
	private final Long shortTokeneExpiredTime = 1000 * 60 * 5L;
	private final Long longTokenExpiredTime = 1000 * 60 * 60 * 24 * 7L;
	
	public String generateJwtToken(User newUser) {
		Date now = new Date();
		return Jwts.builder().setSubject(newUser.getMemUserId()) // 보통 username
				.setHeader(createHeader()).setClaims(createClaims(newUser)) // 클레임, 토큰에 포함될 정보
				.setExpiration(new Date(now.getTime() + shortTokeneExpiredTime)) // 만료일
				.signWith(SignatureAlgorithm.HS256, securityKey).compact();
	}
	
	public String generateRefreshJwtToken(User newUser) {
		Date now = new Date();
		return Jwts.builder().setSubject(newUser.getMemUserId()) // 보통 username
				.setHeader(createHeader()).setClaims(createLongClaims(newUser)) // 클레임, 토큰에 포함될 정보
				.setExpiration(new Date(now.getTime() + longTokenExpiredTime)) // 만료일
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
		claims.put("mem_id", newUser.getMemId());
		claims.put("mem_userid", newUser.getMemUserId()); // username
		claims.put("mem_email", newUser.getMemEmail());
		claims.put("mem_nick", newUser.getMemNick());// 인가정보
		claims.put("role", newUser.getRole());
		return claims;
	}
	
	private Map<String, Object> createLongClaims(User newUser) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("mem_id", newUser.getMemId());
		claims.put("mem_userid", newUser.getMemUserId());
		claims.put("mem_email", newUser.getMemEmail());
		claims.put("mem_nick", newUser.getMemNick());
		claims.put("RefreshToken", newUser.getMemId());
		return claims;
	}

	public Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();
	}

	public int getIdFromToken(String token) {
		return (int) getClaims(token).get("mem_id");
	}
	
	public int isRefreshToken(String token) {

		if(!getClaims(token).containsKey("RefreshToken")) {
			
			return -1;
		}
		return (int) getClaims(token).get("RefreshToken");
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
	
	public String getSecret() {
		return this.securityKey;
	}

}
