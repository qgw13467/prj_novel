package io.team.service;

import java.util.ArrayList;

import io.team.domain.Board;
import io.team.domain.User;

public interface BoardService {
	void register(Board newBoard);

	Board find(int id);

	void modify(int brd_id, Board newBoard);

	void remove(int id);
	
	int getNextBoard();
	
	ArrayList<Board> getBoardList(int pagenum);
}
