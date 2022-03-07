package io.team.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.team.domain.User;
import io.team.jwt.JwtManager;
import io.team.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	
	private final AuthenticationManager authenticationManager;
	
	private final UserMapper userMapper;
	private final JwtManager jwtManager;
	
	
	
	// 로그인 요청하면 로그인 시호를 위해 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("attemp 함수");
		try {
//			BufferedReader bufferedReader = request.getReader();
//			String inputString =null;
//			while((inputString = bufferedReader.readLine())!=null) {
//				System.out.println(inputString);
//			}
			
			ObjectMapper omo = new ObjectMapper();
			User user = omo.readValue(request.getInputStream(), User.class);
//			System.out.println("check1");
//			System.out.println(user);
			
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(user.getMem_userid(), user.getMem_password());
			
			
			//PrincipalDetailsService의 loadUserByUsername() 함수 실행됨
			//DB에 있는 id와 pwd가 일치
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			//authentication 객체가 session영역에 저장됨-> 로그인됨
			
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//			System.out.println("check2");
//			System.out.println(principalDetails.getUser().getMem_userid());
			
			//authentication 객체가 session영역에 저장해햐 하고 그 방법으로 return 하면됨
			//리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고함
			//jwt토큰을 사용하면 세션을 만들 이유가 없으나 권한 처리때문에 session에 넣어줌
			return authentication;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	//attemptAuthentication실행 후 인증이 정상적으로 되었으면 실행
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		User user;
		try {
			
			user = userMapper.read(principalDetails.getUsername(), principalDetails.getPassword());
			String token = jwtManager.generateJwtToken(user);
//			System.out.println(token);
			response.addHeader("Authorization", "Bearer " + token );
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("successfulAuthentication 실행");
	}


	
}
