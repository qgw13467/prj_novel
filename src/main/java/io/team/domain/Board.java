package io.team.domain;

import lombok.Data;

@Data
public class Board {
	private int brdId;
	private int memId;
	private String imgUrl;
	private String memNickname;
	private String brdTitle;
	private String brdContents;
	private int brdState;
	private String brdDatetime;
	private String brdUpdatetime;
	private int brdHit;
	private int brdCommentCount;
	private int brdLike;
	private int brdDislike;
	private int brdNotice;
	private int brdImg;
	private int brdFile;
}	
