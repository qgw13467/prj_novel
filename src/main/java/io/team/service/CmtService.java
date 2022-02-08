package io.team.service;

import java.util.ArrayList;
import org.springframework.stereotype.Repository;


public interface CmtService<T> {
	
	int register(T newCmt, String token);

	int modify(int cmt_id, T newCmt, String token);

	int remove(int id, String token);
	
	int like(int id, String token);
	
	int dislike(int id, String token);
	
	int report(int id, String token);
	
	ArrayList<T> getCmtList(int id, int pagenum);
	
	ArrayList<T> read_replies(int cmt_id);
	
	int getPageNum(int id);
	
	int cmt_reply_count(int brd_cmt_id);
	
	
}
