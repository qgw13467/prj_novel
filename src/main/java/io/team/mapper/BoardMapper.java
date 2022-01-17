package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import io.team.domain.Board;

@Repository
public interface BoardMapper {
	void create(int mem_id, int img_id, String mem_nickname, String brd_title, String brd_contents, int brd_state,
			String brd_datetime, int brd_img, int brd_file);

	Board read(int brd_id);

	void update(int brd_id, int img_id, String mem_nickname, String brd_title, String brd_contents, int brd_state,
			String brd_updatetime, int brd_img, int brd_file);

	void delete(int brd_id);

	int getNextBoard();
	
	ArrayList<Board> getBoards(int pagenum);

}
