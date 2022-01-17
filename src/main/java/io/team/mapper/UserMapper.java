package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import io.team.domain.User;

@Repository
public interface UserMapper {

	void create(String mem_userid, String mem_password, String mem_changepwd, String mem_email, String mem_nick,
			String mem_regist_datetime, String mem_lastlogin_datetime, String mem_icon);

	User read(int mem_id);
	
	void lastlogin(int mem_id);
	
	void update(int mem_id, String mem_userid, String mem_changepwd, String mem_email,
			String mem_nick, int mem_point, int mem_state, int mem_following_nvnum,
			int mem_following_wrnum, int mem_followed, String mem_icon);
	
	void updatepwd(int mem_id,String mem_pasaword);
	
	void delete(int mem_id);

	ArrayList<User> selectAll();

	void chageNickname(int id);

	void chagePwd(int id);

	void uploadImg(Object object);

}
