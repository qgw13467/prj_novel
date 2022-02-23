package io.team.service.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import io.team.domain.Board;
import io.team.domain.User;
import io.team.jwt.JwtManager;
import io.team.mapper.UserMapper;
import io.team.service.UserService;

@Repository
@Service
public class UserServicLogic implements UserService {
	@Autowired
	JwtManager jwtManager;

	@Autowired
	UserMapper userMapper;

	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar time = Calendar.getInstance();
	String format_time1 = format1.format(time.getTime());

	@Override
	public String register(User newUser) {

		if (userMapper.checkIdOverlap(newUser.getMem_userid(), newUser.getMem_nick()) != 0) {
			return "ERROR";
		}
		userMapper.create(newUser.getMem_userid(), newUser.getMem_password(), format_time1, newUser.getMem_email(),
				newUser.getMem_nick(), format_time1, format_time1, newUser.getMem_icon());
		return "OK";
	}

	@Override
	public String makeToken(User newUser) {
		User user;
		try {
			user = userMapper.read(newUser.getMem_userid(), newUser.getMem_password());
			String token = jwtManager.generateJwtToken(user);
			return token;
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public HashMap find(User newUser) {
		userMapper.lastlogin(newUser.getMem_id());
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			User user = userMapper.read(newUser.getMem_userid(), newUser.getMem_password());

			map.put("mem_nick", user.getMem_nick());
			map.put("mem_id", Integer.toString(user.getMem_id()));
			map.put("mem_userid", user.getMem_userid());
			map.put("mem_icon", user.getMem_icon());
			map.put("mem_lastlogin_datetime", user.getMem_lastlogin_datetime());
			return map;
		} catch (Exception e) {
			return map;
		}
	}
	
	public User findByMemid(int mem_id) {
		
		try {
			User user = userMapper.findByMemid(mem_id);
			return user;
		} catch (Exception e) {
			return new User();
		}
	}

	@Override
	public int modify(User newUser, String token) {

		int result = userMapper.update(newUser.getMem_userid(), newUser.getMem_password(), newUser.getMem_email(),
				newUser.getMem_nick(), newUser.getMem_point(), newUser.getState(), newUser.getMem_following_nvnum(),
				newUser.getMem_following_wrnum(), newUser.getMem_followed(), newUser.getMem_icon());
		return result;

	}

	@Override
	public int modify(int id, String pwd, String token) {
		return userMapper.updatepwd(id, pwd);
	}

	@Override
	public int remove(User newUser, String token) {
		int result = userMapper.delete(newUser.getMem_userid(), newUser.getMem_password());
		return result;
	}
	
	public int lastlogin(int mem_id) {
		int result = -1;
		try {
			result= userMapper.lastlogin(mem_id);
			return result;
		}catch (Exception e) {
			return result;
		}

	}
	
	public int changePoint(int mem_id, int point) {
		int result = -1;
		try {
			result= userMapper.changePoint(mem_id, point);
			return result;
		}catch (Exception e) {
			return result;
		}
	}

}
