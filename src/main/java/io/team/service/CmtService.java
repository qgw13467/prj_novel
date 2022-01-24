package io.team.service;

import java.util.ArrayList;

import io.team.domain.BrdCmt;

public interface CmtService {
	
	int register(BrdCmt newCmt, String token);

	int modify(int cmt_id, BrdCmt newCmt, String token);

	int remove(int id, String token);
	
	int like(int id, String token);
	
	int dislike(int id, String token);
	
	int report(int id, String token);
	
	ArrayList<BrdCmt> getCmtList(int id, int pagenum);
	
	ArrayList<BrdCmt> read_reply(int brd_cmt_id);

	ArrayList<BrdCmt> getReplyList(ArrayList<BrdCmt> brdCmtList);
	
	int getPageNum(int id);
}
