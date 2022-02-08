package io.team.domain;

import lombok.Data;

@Data
public class NovelCmt {
	private int nv_cmt_id;
	private int mem_id;
	private int nv_id;
	private int nv_cmt_reply;
	private int nv_cmt_replynum;
	private String mem_nickname;
	private String nv_cmt_contents;
	private int nv_cmt_like;
	private int nv_cmt_dislike;
	private int nv_cmt_blame;
	private String nv_cmt_datetime;
	private String nv_cmt_updatetime;
	private int nv_cmt_state;
}
