package io.team.domain;


import lombok.Data;


@Data
public class User {
	private int mem_id;
	private String mem_userid;
	private String mem_password;
	private String mem_changepwd;
	private String mem_email;
	private String mem_nick;
	private int mem_point;
	private int state;
	private String mem_regist_datetime;
	private String mem_lastlogin_datetime;
	private int mem_following_nvnum;
	private int mem_following_wrnum;
	private int mem_followed;
	private String mem_icon;
	private int mem_is_admin;
}
