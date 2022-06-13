package io.team.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.team.domain.User;
import io.team.service.logic.user.UserServiceLogic;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final UserServiceLogic userServicLogic;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = Optional.ofNullable(userServicLogic.findByMemUserId(username));
		
		if(optionalUser.isEmpty()) {
			User newUser = new User();
			newUser.setMemPassword(bCryptPasswordEncoder.encode(""));
			
			return new PrincipalDetails(newUser);
		}
		User user = optionalUser.orElse(null);
		return new PrincipalDetails(user);
	}

}
