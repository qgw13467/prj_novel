package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import io.team.domain.Board;

@Repository
public interface BoardMapper {
	int create(int mem_id, String img_url, String mem_nickname, String brd_title, String brd_contents, int brd_state,
			String brd_datetime, int brd_img, int brd_file);

	Board read(int brd_id);
	
	int count_hit(int brd_id);
	
	int update(int brd_id, String img_url, String mem_nickname, String brd_title, String brd_contents, int brd_state,
			int brd_img, int brd_file);

	int delete(int brd_id);
	
	ArrayList<Board> getBoards(int pagenum, int rownum);
	int brdcount();
	
	ArrayList<Board> findByTitleContain(String keyword, int pagenum, int rownum);
	int getTitleContainCount(String keyword);
	
	ArrayList<Board> findByContentsContain(String keyword, int pagenum, int rownum);
	int getContentsContainCount(String keyword);
	
	int like(int brd_id);
	int likeMinus(int brd_id);
	
	int dislike(int brd_id);
	int dislikeMinus(int brd_id);
	
	
	int report(int brd_id);
	
	
	int plusCmtCount(int brd_id);
	int minusCmtCount(int brd_id);

}
