package io.team.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.team.domain.User;
import io.team.service.logic.UserServicLogic;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserServicLogic userServicLogic;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userServicLogic.findByUserid(username);
//		System.out.println("check ; loadUserByUsername");
//		System.out.println(user);
		if(user != null) {
			return new PrincipalDetails(user);
		}
		return new PrincipalDetails(user);
	}

}
