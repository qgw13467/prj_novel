package io.team.novel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.team.domain.User;
import io.team.jwt.JwtManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JwtManger 테스트")
public class JwtManagerTest {
	private JwtManager jwtManager;

	@BeforeEach
	void setUp() {
		jwtManager = new JwtManager();
	}

	@Test
	@DisplayName("토큰 생성 및 복호화 테스트")
	void tokenTest() { // given
		
		User user = User.builder()
				.mem_userid("hong01")
				.mem_password("1234")
				.mem_email("hong@gmail.com")
				.build();

			
		final String token = jwtManager.generateJwtToken(user);
		String usernameFromToken = jwtManager.getUsernameFromToken(token);
		System.out.println(token);
		assertEquals("hong01", usernameFromToken);

		
		
	}

}
