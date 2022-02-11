package io.team.service.logic;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import io.team.domain.NovelCmt;
import io.team.service.CmtService;

@Service
public class NvCmtServiceLogic implements CmtService<NovelCmt>{

	@Override
	public int register(NovelCmt newCmt, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modify(int cmt_id, NovelCmt newCmt, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int remove(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int like(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int dislike(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int report(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<NovelCmt> getCmtList(int id, int pagenum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<NovelCmt> read_replies(int cmt_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPageNum(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int cmt_reply_count(int brd_cmt_id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
