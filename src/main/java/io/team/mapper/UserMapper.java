package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import io.team.domain.User;

@Repository
public interface UserMapper {

	int checkIdReduplication(String mem_userid);
	int checkNickReduplication(String mem_nick);
	
	int create(String mem_userid, String mem_password, String mem_changepwd, String mem_email, String mem_nick,
			String mem_regist_datetime, String mem_lastlogin_datetime, String mem_icon);

	User read(String mem_userid, String mem_password);

	User findByMemUserId(String mem_userid);

	User findByMemid(int mem_id);

	ArrayList<String> findTokenByMemid(ArrayList<Integer> item);

	int lastlogin(int mem_id);

	int changePoint(int mem_id, int point);

	int getPoint(int mem_id);

	int update(int mem_id, String mem_email, String mem_nick, String mem_icon);

	int updateToken(String token, int mem_id);

	int updatepwd(int mem_id, String mem_password);

	int delete(String mem_userid, String mem_password);

	ArrayList<User> selectAll();

	int chageNickname(int id, String mem_nick);

	int chagePwd(int id);

	int uploadImg(Object object);

}
