package io.team.domain;

import lombok.Data;

@Data
public class NovelCover {
	private int nvall_id;
	private int img_id;
	private String nvc_title;
	private int nvc_hit;
	private int nvc_reviewpoint;
	private int mvc_reviewcount;
}
