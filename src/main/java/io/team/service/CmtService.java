package io.team.service;

import java.util.ArrayList;
import org.springframework.stereotype.Repository;


public interface CmtService<T, T2> {
	
	int register(T newCmt, String token);

	int modify(int cmt_id, T newCmt, String token);

	int remove(int id, String token);
	
	int report(T2 obj);
	
	ArrayList<T> getCmtList(int id, int pagenum);
	
	ArrayList<T> read_replies(int cmt_id);
	
	int getPageNum(int id);
	
	int cmt_reply_count(int brd_cmt_id);
	
	
}
