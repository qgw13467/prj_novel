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

	int getNextBoard();
	
	ArrayList<Board> getBoards(int pagenum, int pagecount);
	
	int like(int brd_id, int mem_id);
	
	int dislike(int brd_id, int mem_id);
	
	int report(int brd_id, int mem_id);
	
	int brdcount();

}
