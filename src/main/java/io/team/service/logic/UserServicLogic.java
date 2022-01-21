package io.team.service.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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

		if (userMapper.checkIdOverlap(newUser.getMem_userid()) != 0) {
			return "msg:ERROR";
		}
		userMapper.create(newUser.getMem_userid(), newUser.getMem_password(), format_time1, newUser.getMem_email(),
				newUser.getMem_nick(), format_time1, format_time1, newUser.getMem_icon());
		return "msg:OK";
	}

	@Override
	public String find(User newUser) {
		userMapper.lastlogin(newUser.getMem_id());
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
	public void modify( User newUser) {
		userMapper.update(newUser.getMem_userid(), newUser.getMem_password(), newUser.getMem_email(),
				newUser.getMem_nick(), newUser.getMem_point(), newUser.getState(), newUser.getMem_following_nvnum(),
				newUser.getMem_following_wrnum(), newUser.getMem_followed(), newUser.getMem_icon());
	}

	@Override
	public void modify(int id, String pwd) {
		userMapper.updatepwd(id, pwd);

	}

	@Override
	public void remove(User newUser) {
		userMapper.delete(newUser.getMem_userid(),newUser.getMem_password());

	}

}
