package io.team.domain;

import lombok.Data;

@Data
public class NovelCmt {
	private int nvCmtId;
	private int memId;
	private int nvId;
	private int nvCmtReply;
	private int nvCmtReplynum;
	private String memNickname;
	private String nvCmtContents;
	private int nvCmtLike;
	private int nvCmtDislike;
	private int nvCmtBlame;
	private String nvCmtDatetime;
	private String nvCmtUpdatetime;
	private int nvCmtState;
}
