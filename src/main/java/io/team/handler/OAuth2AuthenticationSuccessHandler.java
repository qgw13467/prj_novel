package io.team.handler;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import io.team.auth.PrincipalDetails;
import io.team.domain.User;
import io.team.domain.Enum.PointPurpose;
import io.team.jwt.JwtManager;
import io.team.service.logic.PointServiceLogic;
import io.team.service.logic.user.UserServicLogic;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtManager jwtManager;
	private final PointServiceLogic pointServiceLogic;
	private final UserServicLogic userServicLogic;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		User user = userServicLogic.findByMemUserId(principalDetails.getUser().getMemUserId());

		int attendance_point = 100;
		int check = pointServiceLogic.attend(user.getMemId(), PointPurpose.ATTENDANCE, attendance_point,
				user.getMemLastloginDatetime());

//      System.out.println(user);
//      System.out.println(user.getMem_lastlogin_datetime());
//		System.out.println(check);

		userServicLogic.lastlogin(user.getMemId());
		String token = jwtManager.generateJwtToken(user);
		String RefreshToken = jwtManager.generateJwtToken(user);
		writeTokenResponse(response, token);

//		response.setStatus(307);

		RequestDispatcher rd = request.getRequestDispatcher("/oauth/redirect");
		request.setCharacterEncoding("UTF-8");
		request.setAttribute("RefreshToken", RefreshToken);
		request.setAttribute("Authorization", token);
		request.setAttribute("memNick", user.getMemNick());
		request.setAttribute("memId", user.getMemId());
		request.setAttribute("memUserId", user.getMemUserId());
		request.setAttribute("memIcon", user.getMemIcon());
		request.setAttribute("memLastloginDatetime", user.getMemLastloginDatetime());
		request.setAttribute("memPoint", user.getMemPoint());
		rd.forward(request, response);



	}

	private void writeTokenResponse(HttpServletResponse response, String token) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.addHeader("Authorization", token);

//    var writer = response.getWriter();
//    writer.println(objectMapper.writeValueAsString(token));
//    writer.flush();
	}

//	  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//	        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//	                .map(Cookie::getValue);
//
//	        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
//	            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
//	        }
//
//	        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
//
//	        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
//	        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());
//
//	        OidcUser user = ((OidcUser) authentication.getPrincipal());
//	        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
//	        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();
//
//	        RoleType roleType = hasAuthority(authorities, RoleType.ADMIN.getCode()) ? RoleType.ADMIN : RoleType.USER;
//
//	        Date now = new Date();
//	        AuthToken accessToken = tokenProvider.createAuthToken(
//	                userInfo.getId(),
//	                roleType.getCode(),
//	                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
//	        );
//
//	        // refresh 토큰 설정
//	        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
//
//	        AuthToken refreshToken = tokenProvider.createAuthToken(
//	                appProperties.getAuth().getTokenSecret(),
//	                new Date(now.getTime() + refreshTokenExpiry)
//	        );
//
//	        // DB 저장
//	        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userInfo.getId());
//	        if (userRefreshToken != null) {
//	            userRefreshToken.setRefreshToken(refreshToken.getToken());
//	        } else {
//	            userRefreshToken = new UserRefreshToken(userInfo.getId(), refreshToken.getToken());
//	            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
//	        }
//
//	        int cookieMaxAge = (int) refreshTokenExpiry / 60;
//
//	        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
//	        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);
//
//	        return UriComponentsBuilder.fromUriString(targetUrl)
//	                .queryParam("token", accessToken.getToken())
//	                .build().toUriString();
//	    }

}
