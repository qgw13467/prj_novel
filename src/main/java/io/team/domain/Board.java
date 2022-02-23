package io.team.domain;

import lombok.Data;

@Data
public class Board {
	private int brd_id;
	private int mem_id;
	private String img_url;
	private String mem_nickname;
	private String brd_title;
	private String brd_contents;
	private int brd_state;
	private String brd_datetime;
	private String brd_updatetime;
	private int brd_hit;
	private int brd_comment_count;
	private int brd_like;
	private int brd_dislike;
	private int brd_notice;
	private int brd_img;
	private int brd_file;
}	
