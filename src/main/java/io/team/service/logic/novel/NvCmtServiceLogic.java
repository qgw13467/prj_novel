package io.team.service.logic.novel;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.team.domain.NovelCmt;
import io.team.jwt.JwtManager;
import io.team.mapper.NvCmtMapper;
import io.team.service.CmtService;

@Service
public class NvCmtServiceLogic implements CmtService<NovelCmt>{
	
	@Autowired
	JwtManager jwtManager;

	@Autowired
	NvCmtMapper nvCmtMapper;
	
	@Override
	public int register(NovelCmt newCmt, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		
		if(newCmt.getNv_cmt_reply()!=0) {
			cmt_reply_count(newCmt.getNv_cmt_reply());
		}
		
		if (newCmt.getMem_id() == mem_id) {
			int result = nvCmtMapper.create(newCmt.getNv_id(), newCmt.getMem_id(), newCmt.getNv_cmt_reply(),
					newCmt.getMem_nickname(), newCmt.getNv_cmt_contents(), newCmt.getNv_cmt_state());
			return result;
		} else {
			return -1;
		}

	}

	@Override
	public int modify(int cmt_id, NovelCmt newCmt, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		
		NovelCmt brdCmt = nvCmtMapper.read(cmt_id);
		
		if (brdCmt.getMem_id() == mem_id) {
			int result = nvCmtMapper.update(cmt_id, newCmt.getNv_cmt_contents(),
					newCmt.getNv_cmt_state());
			return result;
		} else {
			return -1;
		}
	}

	@Override
	public int remove(int id, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		NovelCmt nvCmt = nvCmtMapper.read(id);

		if (nvCmt.getMem_id() == mem_id) {
			int result = nvCmtMapper.delete(id);
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
	public ArrayList<NovelCmt> getCmtList(int nv_id, int pagenum) {
		int pagecount = 10;
		return nvCmtMapper.read_cmts(nv_id, (pagenum - 1) * pagecount, pagecount);
	}

	@Override
	public ArrayList<NovelCmt> read_replies(int nv_cmt_reply) {
		ArrayList<NovelCmt> result = nvCmtMapper.read_reply(nv_cmt_reply);
		return result;
	}

	@Override
	public int getPageNum(int nv_id) {
		int pagecount = 10;
		int nvNum = nvCmtMapper.cmtcount(nv_id);
		int result = nvNum / pagecount;
		
		if (nvNum % pagecount != 0) {
			result = nvNum / pagecount + 1;
			return result;
		} else return result;
	}

	@Override
	public int cmt_reply_count(int nv_cmt_id) {
		int result = nvCmtMapper.cmt_reply_count(nv_cmt_id);
		return result;
		
	}

}
