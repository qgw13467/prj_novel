package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import io.team.domain.User;

@Repository
public interface UserMapper {
	
	int checkIdOverlap(String mem_userid, String mem_nick);
	
	int create(String mem_userid, String mem_password, String mem_changepwd, String mem_email, String mem_nick,
			String mem_regist_datetime, String mem_lastlogin_datetime, String mem_icon);

	User read(String mem_userid, String mem_password);
	
	User findById(String mem_userid);
	
	int lastlogin(int mem_id);
	
	int update(String mem_userid, String mem_password, String mem_email,
			String mem_nick, int mem_point, int mem_state, int mem_following_nvnum,
			int mem_following_wrnum, int mem_followed, String mem_icon);
	
	int updatepwd(int mem_id,String mem_pasaword);
	
	int delete(String mem_userid, String mem_password);

	ArrayList<User> selectAll();

	int chageNickname(int id);

	int chagePwd(int id);

	int uploadImg(Object object);

}
