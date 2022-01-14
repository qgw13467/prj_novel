package io.team.service;

import java.util.ArrayList;

import io.team.domain.Board;

public interface BoardService {
	void register(Board newBoard);

	Board find(int id);

	void modify(Board newBoard);

	void remove(int id);
	
	
	ArrayList<Board> getBoards(int pagenum);
}
