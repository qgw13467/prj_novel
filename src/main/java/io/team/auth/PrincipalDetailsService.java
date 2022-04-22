package io.team.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.team.domain.User;
import io.team.service.logic.user.UserServicLogic;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final UserServicLogic userServicLogic;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userServicLogic.findByMemUserId(username);

		if (user != null) {
			return new PrincipalDetails(user);
		}
		return new PrincipalDetails(user);
	}

}
