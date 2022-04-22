package io.team.domain;

import lombok.Data;

@Data
public class BrdCmt {
	private int brdCmtId;
	private int brdId;
	private int memId;
	private int brdCmtReply;
	private int brdCmtReplynum;
	private String memNickname;
	private String brdCmtContents;
	private int brdCmtLike;
	private int brdCmtDislike;
	private int brdCmtBlame;
	private String brdCmtDatetime;
	private String brdCmtUpdatetime;
	private int brdCmtState;
	
}
