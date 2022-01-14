package io.team.service;

import java.util.ArrayList;
import io.team.domain.User;


public interface UserService {
	
	String register(User newUser);
	void modify(User newUser);
	void remove(int id);
	
	User find(int id);
	ArrayList<User> selctAllUsers();
	
}
