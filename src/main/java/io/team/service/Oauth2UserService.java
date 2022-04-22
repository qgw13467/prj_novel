package io.team.service;


import java.util.Random;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import io.team.auth.PrincipalDetails;
import io.team.domain.User;
import io.team.service.logic.user.UserServicLogic;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {
	
	private final UserServicLogic userServicLogic;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		

		OAuth2User oAuth2User = super.loadUser(userRequest);
		String provider = userRequest.getClientRegistration().getClientName();
		String providerid = oAuth2User.getAttribute("sub");
		String username = provider + "_" + providerid;
		String email = oAuth2User.getAttribute("email");
		String password = bCryptPasswordEncoder.encode(makeRandomString());

		
		//새로운 사용자가 회원가입할 떄 닉네임이 같으면 생성되지 않는 문제 해결할 것
		User user = userServicLogic.findByMemUserId(providerid);
		if(user == null) {
			user = User.builder()
					.memUserId(username)
					.memPassword(password)
					.memEmail(email)
					.memNick("guest_"+providerid)
					.memIcon("1")
					.build();
			userServicLogic.register(user);
		}
		
		//return super.loadUser(userRequest);
		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
	
	//랜덤 비밀번호 생성
	public String makeRandomString() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 20;
		Random random = new Random();
		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		return generatedString;
	}

}
