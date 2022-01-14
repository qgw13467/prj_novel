package io.team.service;

import java.util.ArrayList;

import io.team.domain.Board;

public interface BoardService {
	String register(Board newBoard);

	void modify(Board newBoard);

	void remove(int id);

	Board find(int id);

	ArrayList<Board> selctAllUsers();
}
