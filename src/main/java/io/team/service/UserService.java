package io.team.service;


import io.team.domain.User;

public interface UserService {

	String register(User newUser);

	String find(User newUser);

	void modify(int id,User newUser);
	
	void modify(int id, String pwd);

	void remove(int id);


}
