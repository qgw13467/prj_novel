package io.team.service;

import java.util.ArrayList;

import io.team.domain.BrdCmt;

public interface CmtService {
	int register(BrdCmt newBoard);


	int modify(int brd_id, BrdCmt newBoard);

	int remove(int id);
	
	int like(int id);
	
	int dislike(int id);
	
	int report(int id);
	
	ArrayList<BrdCmt> getBoardList(int pagenum);
}
