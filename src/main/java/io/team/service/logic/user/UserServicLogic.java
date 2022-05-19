package io.team.service.logic.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.team.domain.User;
import io.team.jwt.JwtManager;
import io.team.mapper.UserMapper;
import io.team.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServicLogic implements UserService {

	private final JwtManager jwtManager;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserMapper userMapper;

	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar time = Calendar.getInstance();
	String format_time1 = format1.format(time.getTime());

	@Override
	public String register(User newUser) {

		if (userMapper.checkIdReduplication(newUser.getMemUserId()) != 0) {
			return "id reduplication";
		}
		else if(userMapper.checkNickReduplication(newUser.getMemNick())!=0){
			return "nickname reduplication";
		}
		userMapper.create(newUser.getMemUserId(), newUser.getMemPassword(), format_time1, newUser.getMemEmail(),
				newUser.getMemNick(), format_time1, format_time1, newUser.getMemIcon());
		return "OK";
	}

	@Override
	public String makeToken(User newUser) {
		User user;
		try {

			user = userMapper.read(newUser.getMemUserId(), newUser.getMemPassword());
			String token = jwtManager.generateJwtToken(user);
			return token;
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public HashMap find(User newUser) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			User user = userMapper.read(newUser.getMemUserId(), newUser.getMemPassword());

			map.put("memNick", user.getMemNick());
			map.put("memId", Integer.toString(user.getMemId()));
			map.put("memUserId", user.getMemUserId());
			map.put("memIcon", user.getMemIcon());
			map.put("memLastloginDatetime", user.getMemLastloginDatetime());
			return map;
		} catch (Exception e) {
			return map;
		}
	}

	public User findByMemId(int mem_id) {

		try {
			User user = userMapper.findByMemid(mem_id);
			return user;
		} catch (Exception e) {
			return new User();
		}
	}

	public User findByMemUserId(String userid) {

		try {
			User user = userMapper.findByMemUserId(userid);

			return user;
		} catch (Exception e) {
			return new User();
		}
	}

	public ArrayList<String> findTokenByMemid(ArrayList<Integer> memids) {
		ArrayList<String> result = new ArrayList<>();

		result = userMapper.findTokenByMemid(memids);

		return result;
	}

	@Override
	public int modify(User newUser, String token) {
		int result = userMapper.update(newUser.getMemId(), newUser.getMemEmail(), newUser.getMemNick(),
				newUser.getMemIcon());
		return result;

	}

	@Override
	public int modify(int id, String encPwd, String pwd) {
		User user= userMapper.findByMemid(id);
		if(bCryptPasswordEncoder.matches(pwd, user.getMemPassword())) {
			return -2;
		}
		
		return userMapper.updatepwd(id, encPwd);
	}

	public int updateToken(String token, int mem_id) {

		int result = userMapper.updateToken(token, mem_id);

		return result;
	}

	@Override
	public int remove(User newUser, String token) {
		int result = userMapper.delete(newUser.getMemUserId(), newUser.getMemPassword());
		return result;
	}

	public int lastlogin(int mem_id) {
		int result = -1;
		try {
			result = userMapper.lastlogin(mem_id);
			return result;
		} catch (Exception e) {
			return result;
		}

	}

	public int changePoint(int mem_id, int point) {
		int result = -1;
		try {
			result = userMapper.changePoint(mem_id, point);
			return result;
		} catch (Exception e) {
			return result;
		}
	}

	public int getPoint(int mem_id) {
		int result = -1;
		try {
			result = userMapper.getPoint(mem_id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}

}
