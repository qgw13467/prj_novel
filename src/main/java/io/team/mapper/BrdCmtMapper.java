package io.team.mapper;

import java.util.ArrayList;

import io.team.domain.BrdCmt;

public interface BrdCmtMapper {
	int create(int brd_id, int mem_id, int brd_cmt_reply, String mem_nickname,
			String brd_cmt_contents, int brd_cmt_state);
	
	BrdCmt read(int brd_cmt_id);
	
	ArrayList<BrdCmt> read_cmts(int brd_id, int pagenum, int pagecount);
	
	ArrayList<BrdCmt> read_reply(int brd_cmt_reply);
	
	int update(int brd_cmt_id, String brd_cmt_contents, int brd_cmt_state);

	int delete(int brd_cmt_id);
	
	int like(int brd_cmt_id, int mem_id);
	
	int dislike(int brd_cmt_id, int mem_id);
	
	int report(int brd_cmt_id, int mem_id);
	
	int cmtcount(int brd_id);
	
	int cmt_reply_count(int brd_cmt_id);
}
