package io.team.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import io.team.auth.JwtAuthenticationFilter;
import io.team.filter.MyFilter01;
import io.team.handler.OAuth2SuccessHandler;
import io.team.jwt.JwtManager;
import io.team.mapper.UserMapper;
import io.team.service.Oauth2UserService;
import io.team.service.logic.PointServiceLogic;
import io.team.service.logic.UserServicLogic;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final CorsConfig corsConfig;
	private final UserMapper userMapper;
	private final JwtManager jwtManager;
	private final PointServiceLogic pointServiceLogic;
	private final UserServicLogic userServicLogic;
	private final Oauth2UserService oauth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/user/*");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new MyFilter01(), SecurityContextPersistenceFilter.class);
		http.addFilter(corsConfig.corsFilter());
		http.addFilter(new JwtAuthenticationFilter(authenticationManager(), pointServiceLogic, userMapper, jwtManager,
				userServicLogic));
		http.httpBasic().disable() // security에서 기본으로 생성하는 login페이지 사용 안 함
				.csrf().disable() // csrf 사용 안 함 == REST API 사용하기 때문에
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT인증사용하므로 세션 사용 함
				.and().formLogin().disable().authorizeRequests()
				.antMatchers("/user/test").hasRole("ADMIN")
				.antMatchers("/google").authenticated()
				.anyRequest().permitAll()
				.and().oauth2Login()
				//.defaultSuccessUrl("/")
				.successHandler(oAuth2SuccessHandler)
				.userInfoEndpoint()
				.userService(oauth2UserService);
	}



}
