package io.team.novel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import io.team.domain.User;
import io.team.jwt.JwtManager;
import io.team.mapper.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JwtManger 테스트")
@Configuration
public class JwtManagerTest {
	
	@Autowired
	private JwtManager jwtManager;

	
	@Autowired
	private UserMapper userMapper;
	
	
	@DisplayName("토큰 생성 및 복호화 테스트")
	//@Test
	void tokenTest() { // given

		User user = User.builder()
				.mem_userid("test2")
				.mem_password("1234").build();

		User newUser = userMapper.read(user.getMem_userid(), user.getMem_password());
		String token = jwtManager.generateJwtToken(user);

		String usernameFromToken = jwtManager.getUserIdFromToken(token);
		System.out.println(token);
		System.out.println(usernameFromToken);
		System.out.println(jwtManager.getClaims(token));
		assertEquals("hong01", usernameFromToken);

	}

}
