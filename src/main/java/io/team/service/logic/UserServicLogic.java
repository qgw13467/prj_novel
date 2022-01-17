package io.team.service.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import io.team.domain.User;
import io.team.mapper.UserMapper;
import io.team.service.UserService;

@Repository
@Service
public class UserServicLogic implements UserService {

	@Autowired
	UserMapper userMapper;

	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar time = Calendar.getInstance();
	String format_time1 = format1.format(time.getTime());

	@Override
	public void register(User newUser) {
		userMapper.create(newUser.getMem_userid(), newUser.getMem_password(), format_time1, newUser.getMem_email(),
				newUser.getMem_nick(), format_time1, format_time1, newUser.getMem_icon());

	}

	@Override
	public User find(int mem_id) {
		userMapper.lastlogin(mem_id);
		return userMapper.read(mem_id);
	}

	@Override
	public void modify(int mem_id,User newUser) {
		userMapper.update(mem_id, newUser.getMem_userid(), newUser.getMem_changepwd(),
				newUser.getMem_email(), newUser.getMem_nick(), newUser.getMem_point(), newUser.getState(),
				newUser.getMem_following_nvnum(), newUser.getMem_following_wrnum(), newUser.getMem_followed(),
				newUser.getMem_icon());
	}

	@Override
	public void modify(int id, String pwd) {
		userMapper.updatepwd(id, pwd);

	}

	@Override
	public void remove(int id) {
		userMapper.delete(id);

	}

}
