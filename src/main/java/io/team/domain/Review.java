package io.team.domain;

import lombok.Data;

@Data
public class Review {
	private int rv_id;
	private int nv_id;
	private int mem_id;
	private int rv_point;
	private String rv_datetime;
	private String rv_updatetime;
}
