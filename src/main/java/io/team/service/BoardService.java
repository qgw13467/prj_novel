package io.team.service;

import java.util.ArrayList;

import io.team.domain.Board;
import io.team.domain.User;

public interface BoardService {
	int register(Board newBoard, String token);

	Board find(int id);

	int modify(int brd_id, Board newBoard, String token);

	int remove(int id, String token);
	
	int getNextBoard();
	
	ArrayList<Board> getBoardList(int pagenum);
	
	int like(int id, String token);
	
	int dislike(int id, String token);
	
	int report(int id, String token);
	
	int getPageNum();
}
