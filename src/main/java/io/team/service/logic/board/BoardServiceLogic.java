package io.team.service.logic.board;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.team.domain.Board;
import io.team.domain.User;
import io.team.jwt.JwtManager;
import io.team.mapper.BoardMapper;
import io.team.service.WriteService;

@Service
public class BoardServiceLogic implements WriteService<Board> {

	@Autowired
	JwtManager jwtManager;

	@Autowired
	BoardMapper boardMapper;

	@Override
	public int register(Board newBoard, String token) {


		int mem_id = jwtManager.getIdFromToken(token);
		if (newBoard.getMem_id() == mem_id) {
			int result = boardMapper.create(newBoard.getMem_id(), newBoard.getImg_url(), newBoard.getMem_nickname(),
					newBoard.getBrd_title(), newBoard.getBrd_contents(), newBoard.getBrd_state(),
					newBoard.getBrd_datetime(), newBoard.getBrd_img(), newBoard.getBrd_file());
			return result;
		} else {
			return -1;
		}
	}

	@Override
	public Board find(int id) {
		boardMapper.count_hit(id);
		return boardMapper.read(id);
	}

	@Override
	public int modify(int brd_id, Board newBoard, String token) {
		newBoard.setBrd_id(brd_id);
		

		Board board = boardMapper.read(brd_id);
		int mem_id = jwtManager.getIdFromToken(token);
		if (board.getMem_id() == mem_id) {
			int result = boardMapper.update(newBoard.getBrd_id(), newBoard.getImg_url(), newBoard.getMem_nickname(),
					newBoard.getBrd_title(), newBoard.getBrd_contents(), newBoard.getBrd_state(), newBoard.getBrd_img(),
					newBoard.getBrd_file());

			return result;

		} else {
			return -1;
		}
	}

	@Override
	public int remove(int id, String token) {

		int mem_id = jwtManager.getIdFromToken(token);
		Board board = boardMapper.read(id);

		if (board.getMem_id() == mem_id) {

			return boardMapper.delete(id);

		} else {

			return -1;
		}

	}

	@Override
	public ArrayList<Board> getList(int pagenum) {
		int pagecount = 10;
		
		return boardMapper.getBoards((pagenum - 1) * pagecount, pagecount);
	}


	@Override
	public int getPageNum() {
		int pagecount = 10;
		int brdNum = boardMapper.brdcount();
		int result = brdNum / pagecount;

		if (brdNum % pagecount != 0) {
			result = brdNum / pagecount + 1;
			return result;
		} else
			return result;

	}

	
	public int like(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int dislike(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int report(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

}
