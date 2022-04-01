package io.team.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import io.team.domain.User;
import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private User user;
	private Map<String, Object> attributes;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	public PrincipalDetails(User user,Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { 
		Collection<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority("USER"));

		return authorities;
	}

	@Override
	public String getPassword() { 
		
		return user.getMem_password();
	}

	@Override
	public String getUsername() {
		return user.getMem_userid();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		
		return attributes;
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}
}
