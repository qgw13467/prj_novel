package io.team.service;

import java.util.HashMap;
import org.springframework.stereotype.Repository;
import io.team.domain.User;


public interface UserService {

	String register(User newUser);

	HashMap find(User newUser);
	
	String makeToken(User newUser);

	int modify(User newUser, String token);

	int modify(int id, String pwd, String token);

	int remove(User newUser, String token);

}
