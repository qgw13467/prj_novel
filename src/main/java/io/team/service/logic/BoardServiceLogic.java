package io.team.service.logic;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.team.domain.Board;
import io.team.domain.User;
import io.team.mapper.BoardMapper;
import io.team.service.BoardService;

@Service
public class BoardServiceLogic implements BoardService {

	@Autowired
	BoardMapper boardMapper;

	@Override
	public void register( Board newBoard) {
		if(newBoard.getImg_id()==0) {
			newBoard.setImg_id(1);
		}
		
		boardMapper.create(newBoard.getMem_id(), newBoard.getImg_id(), newBoard.getMem_nickname(),
				newBoard.getBrd_title(), newBoard.getBrd_contents(), newBoard.getBrd_state(),
				newBoard.getBrd_datetime(), newBoard.getBrd_img(), newBoard.getBrd_file());
	}

	@Override
	public Board find(int id) {

		return boardMapper.read(id);
	}

	@Override
	public void modify(int brd_id, Board newBoard) {
		newBoard.setBrd_id(brd_id);
		if(newBoard.getImg_id()==0) {
			newBoard.setImg_id(1);
		}
		boardMapper.update(newBoard.getBrd_id(), newBoard.getImg_id(), newBoard.getMem_nickname(),
				newBoard.getBrd_title(), newBoard.getBrd_contents(), newBoard.getBrd_state(),
				newBoard.getBrd_updatetime(), newBoard.getBrd_img(), newBoard.getBrd_file());
	}

	@Override
	public void remove(int id) {
		boardMapper.delete(id);
	}

	@Override
	public ArrayList<Board> getBoardList(int pagenum) {
		
		return boardMapper.getBoards(pagenum);
	}

	@Override
	public int getNextBoard() {
		return boardMapper.getNextBoard();
	}

}
