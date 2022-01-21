package io.team.domain;

import lombok.Data;

@Data
public class BrdCmt {
	private int brd_cmt_id;
	private int brd_id;
	private int mem_id;
	private int brd_cmt_reply;
	private int brd_cmt_replynum;
	private String mem_nickname;
	private String brd_cmt_contents;
	private int brd_cmt_like;
	private int brd_cmt_dislike;
	private int brd_cmt_blame;
	private String brd_cmt_datetime;
	private String brd_cmt_updatetime;
	private int brd_cmt_state;
	
}
