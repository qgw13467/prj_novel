package io.team.service.logic;

import java.util.ArrayList;

import org.apache.catalina.servlets.CGIServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.team.domain.Board;
import io.team.domain.User;
import io.team.mapper.UserMapper;
import io.team.service.UserService;

@Service
public class UserServicLogic implements UserService {

	@Autowired
	UserMapper userMapper;

	@Override
	public void register(User newUser) {
		userMapper.create(newUser.getMem_id(), newUser.getMem_userid(), newUser.getMem_password(),
				newUser.getMem_email(), newUser.getMem_nick(), newUser.getMem_regist_datetime(), newUser.getMem_icon());

	}

	@Override
	public User find(int id) {

		return userMapper.read(id);
	}

	@Override
	public void modify(User newUser) {
		userMapper.update(newUser.getMem_id(), newUser.getMem_userid(), newUser.getMem_changepwd(),
				newUser.getMem_email(), newUser.getMem_nick(), newUser.getMem_point(), newUser.getState(),
				newUser.getMem_lastlogin_datetime(), newUser.getMem_following_nvnum(), newUser.getMem_following_wrnum(),
				newUser.getMem_followed(), newUser.getMem_icon());
	}
	
	@Override
	public void modify(int id, String pwd) {
		userMapper.updatepwd(id, pwd);
		
	}

	@Override
	public void remove(int id) {
		userMapper.delete(id);

	}

	@Override
	public ArrayList<User> selctAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}



}
