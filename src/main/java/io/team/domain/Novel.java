package io.team.domain;

import lombok.Data;

@Data
public class Novel {
	private int nv_id;
	private int mem_id;
	private int img_id;
	private String nv_writer;
	private String nv_title;
	private String nv_contents;
	private int nv_hit;
	private int nv_comment_count;
	private int nv_reviewpoint;
	private int nv_reviewcount;
	private int nv_state;
	private String nv_datetime;
	private String nv_updatetime;
}
