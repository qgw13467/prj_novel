package io.team.service.logic;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.team.domain.BrdCmt;
import io.team.jwt.JwtManager;
import io.team.mapper.BrdCmtMapper;
import io.team.service.CmtService;


@Service
public class BrdCmtServiceLogic implements CmtService {

	@Autowired
	JwtManager jwtManager;

	@Autowired
	BrdCmtMapper brdCmtMapper;
	


	@Override
	public int register(BrdCmt newCmt, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		if (newCmt.getMem_id() == mem_id) {

			int result = brdCmtMapper.create(newCmt.getBrd_id(), newCmt.getMem_id(), newCmt.getBrd_cmt_reply(),
					newCmt.getMem_nickname(), newCmt.getBrd_cmt_contents(), newCmt.getBrd_cmt_state());
			return result;
		} else {
			return -1;
		}

	}

	@Override
	public ArrayList<BrdCmt> getCmtList(int brd_id, int pagenum) {
		int pagecount = 10;
		ArrayList<BrdCmt> result = brdCmtMapper.read_cmts(brd_id, pagenum, pagecount);
		return result;
	}

	@Override
	public ArrayList<BrdCmt> getReplyList(ArrayList<BrdCmt> brdCmtList) {

		ArrayList<BrdCmt> result = new ArrayList<BrdCmt>();
		for(BrdCmt brdCmt:brdCmtList) {
			ArrayList<BrdCmt> temp= new ArrayList<BrdCmt>();
			temp=read_reply(brdCmt.getBrd_cmt_id());
			result.addAll(temp);
		}
		return result;
	}

	@Override
	public ArrayList<BrdCmt> read_reply(int brd_cmt_reply) {
		ArrayList<BrdCmt> result = brdCmtMapper.read_reply(brd_cmt_reply);
		return result;
	}

	@Override
	public int modify(int cmt_id, BrdCmt newCmt, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		if (newCmt.getMem_id() == mem_id) {
			int result = brdCmtMapper.update(newCmt.getBrd_cmt_id(), newCmt.getBrd_cmt_contents(),
					newCmt.getBrd_cmt_state());
			return result;
		} else {
			return -1;
		}
	}

	@Override
	public int remove(int id, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		BrdCmt brdCmt = brdCmtMapper.read(id);

		if (brdCmt.getMem_id() == mem_id) {
			int result = brdCmtMapper.delete(id);
			return result;
		} else {
			return -1;
		}
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
	public int getPageNum(int brd_id) {
		int pagecount = 10;
		int brdNum = brdCmtMapper.cmtcount(brd_id);
		int result = brdNum / pagecount;
		
		if (brdNum % pagecount != 0) {
			result = result / pagecount + 1;
			return result;
		} else return result;
	}

}
