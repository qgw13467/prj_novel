package io.team.service.logic;

import java.util.ArrayList;

import org.apache.catalina.servlets.CGIServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.team.domain.User;
import io.team.mapper.UserMapper;
import io.team.service.UserService;

@Service
public class UserServicLogic implements UserService {
	
	@Autowired
	UserMapper userMapper;
	


	@Override
	public String register(User newUser) {
		
		return userMapper.create(newUser);
	}

	@Override
	public void modify(User newUser) {
		userMapper.update(newUser);
	}

	@Override
	public void remove(int id) {
		userMapper.delete(id);
		
	}

	@Override
	public User find(int id) {

		return userMapper.retrive(id);
	}
	
	@Override
	public ArrayList<User> selctAllUsers() {
		
		return userMapper.selectAll();
	}

}
