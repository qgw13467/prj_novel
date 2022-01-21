package io.team.service;


import java.util.HashMap;

import io.team.domain.User;

public interface UserService {

	String register(User newUser);
	
	
	HashMap find(User newUser);
	
	String makeToken(User newUser);
	
	void modify(User newUser);
	
	void modify(int id, String pwd);

	void remove(User newUser);


}
