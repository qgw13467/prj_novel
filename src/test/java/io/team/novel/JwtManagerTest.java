package io.team.novel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import io.team.domain.User;
import io.team.jwt.JwtManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JwtManger 테스트")
@Configuration
public class JwtManagerTest {
	private JwtManager jwtManager;

	@Value("${JWT.SECRET}")
	public String secretString;

	@BeforeEach
	void setUp() {
		jwtManager = new JwtManager();
	}

	
	@DisplayName("토큰 생성 및 복호화 테스트")
	void tokenTest() { // given

		User user = User.builder().mem_userid("hong01").mem_password("1234").mem_email("hong@gmail.com").build();

		final String token = jwtManager.generateJwtToken(user);
		String usernameFromToken = jwtManager.getUserIdFromToken(token);
		System.out.println(token);
		System.out.println(usernameFromToken);
		System.out.println(jwtManager.getClaims(token));
		assertEquals("hong01", usernameFromToken);

	}

}
