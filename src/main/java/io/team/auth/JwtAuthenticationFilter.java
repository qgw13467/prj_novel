package io.team.auth;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.team.domain.User;
import io.team.domain.Enum.PointPurpose;
import io.team.jwt.JwtManager;
import io.team.mapper.UserMapper;
import io.team.service.logic.PointServiceLogic;
import io.team.service.logic.user.UserServicLogic;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final PointServiceLogic pointServiceLogic;
	private final UserMapper userMapper;
	private final JwtManager jwtManager;
	private final UserServicLogic userServicLogic;


	
	// 로그인 요청하면 로그인 시도를 위해 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
//			BufferedReader bufferedReader = request.getReader();
//			String inputString =null;
//			while((inputString = bufferedReader.readLine())!=null) {
//				System.out.println(inputString);
//			}

			ObjectMapper omo = new ObjectMapper();
			
			User user = omo.readValue(request.getInputStream(), User.class);
			
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					user.getMemUserId(), user.getMemPassword());
			
			
			// PrincipalDetailsService의 loadUserByUsername() 함수 실행됨
			// DB에 있는 id와 pwd가 일치
			Authentication authentication = authenticationManager.authenticate(authenticationToken);

			// authentication 객체가 session영역에 저장됨-> 로그인됨
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			// authentication 객체가 session영역에 저장해햐 하고 그 방법으로 return 하면됨
			// 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고함
			// jwt토큰을 사용하면 세션을 만들 이유가 없으나 권한 처리때문에 session에 넣어줌
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// attemptAuthentication실행 후 인증이 정상적으로 되었으면 실행
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		HashMap<String, String> map = new HashMap<>();
		HashMap<String, String> result = new HashMap<>();
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		User user;
		try {
			
			user = userMapper.read(principalDetails.getUsername(), principalDetails.getPassword());
			map = userServicLogic.find(user);

			result.put("\"msg\"","\"OK\"");
			int attendance_point = 100;
			int check = pointServiceLogic.attend(Integer.parseInt(map.get("memId")), PointPurpose.ATTENDANCE, attendance_point, map.get("memLastloginDatetime"));
			
			if(check == 1) {
				result.put("\"attendance point\"", ""+attendance_point);
				
			}
			else {
				result.put("\"attendance point\"", "0");
			}

			userServicLogic.lastlogin(user.getMemId());
			String token = jwtManager.generateJwtToken(user);
//			System.out.println(token);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("charset=utf-8");

			response.addCookie(new Cookie("memNick", URLEncoder.encode(user.getMemNick(), "utf-8")));
			response.addCookie(new Cookie("memId", URLEncoder.encode(Integer.toString(user.getMemId()), "utf-8")));
			response.addCookie(new Cookie("memUserid", URLEncoder.encode(user.getMemUserId(), "utf-8")));
			response.addCookie(new Cookie("memIcon", URLEncoder.encode(user.getMemIcon(), "utf-8")));
			response.addCookie(new Cookie("memPoint", URLEncoder.encode(Integer.toString(user.getMemPoint()), "utf-8")));
			String date = URLEncoder.encode(user.getMemLastloginDatetime(), "utf-8");
			response.addCookie(
					new Cookie("memLastloginDatetime", date.substring(0,23)));
			
			
			response.getWriter().print(result);
			response.addHeader("Authorization", token);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("successfulAuthentication 실행");
	}



}
