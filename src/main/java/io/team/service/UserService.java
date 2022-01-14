package io.team.service;

import java.util.ArrayList;


import io.team.domain.User;

public interface UserService {

	void register(User newUser);

	User find(int id);

	void modify(User newUser);
	
	void modify(int id, String pwd);

	void remove(int id);

	ArrayList<User> selctAllUsers();

}
