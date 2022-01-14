package io.team.service.logic;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.team.domain.Board;
import io.team.mapper.BoardMapper;
import io.team.service.BoardService;

@Service
public class BoardServiceLogic implements BoardService {

	@Autowired
	BoardMapper boardMapper;

	@Override
	public void register(Board newBoard) {
		boardMapper.create(newBoard.getBrd_id(), newBoard.getImg_id(), newBoard.getMem_nickname(),
				newBoard.getBrd_title(), newBoard.getBrd_contents(), newBoard.getBrd_state(),
				newBoard.getBrd_datetime(), newBoard.getBrd_img(), newBoard.getBrd_file());
	}

	@Override
	public Board find(int id) {

		return boardMapper.read(id);
	}

	@Override
	public void modify(Board newBoard) {
		boardMapper.update(newBoard.getBrd_id(), newBoard.getImg_id(), newBoard.getMem_nickname(),
				newBoard.getBrd_title(), newBoard.getBrd_contents(), newBoard.getBrd_state(),
				newBoard.getBrd_updatetime(), newBoard.getBrd_img(), newBoard.getBrd_file());
	}

	@Override
	public void remove(int id) {
		boardMapper.delete(id);
	}

	@Override
	public ArrayList<Board> getBoards(int pagenum) {
		// TODO Auto-generated method stub
		return null;
	}

}
