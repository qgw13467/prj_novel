package io.team.service.logic.board;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.team.Repository.Board.BrdCmtReportRepository;
import io.team.domain.BrdCmt;
import io.team.domain.BrdCmtReport;
import io.team.domain.BrdReport;
import io.team.jwt.JwtManager;
import io.team.mapper.BrdCmtMapper;
import io.team.service.CmtService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BrdCmtServiceLogic implements CmtService<BrdCmt, BrdCmtReport> {


	private final JwtManager jwtManager;

	private final BrdCmtMapper brdCmtMapper;
	
	private final BrdCmtReportRepository brdCmtReportRepository;

	@Override
	public int register(BrdCmt newCmt, String token) {

		
		try {
			if(newCmt.getBrdCmtReply()!=0) {
				cmt_reply_count(newCmt.getBrdCmtReply());
			}
			int result = brdCmtMapper.create(newCmt.getBrdId(), newCmt.getMemId(), newCmt.getBrdCmtReply(),
					newCmt.getMemNickname(), newCmt.getBrdCmtContents(), newCmt.getBrdCmtState());
			return result;
		} catch (Exception e) {
			return -1;
		}

	}

	@Override
	public ArrayList<BrdCmt> getCmtList(int brd_id, int pagenum) {
		int pagecount = 10;
		return brdCmtMapper.read_cmts(brd_id, (pagenum - 1) * pagecount, pagecount);
	}
	
	public Optional<BrdCmt> findCmt(int brd_cmt_id) {
		Optional<BrdCmt> optBrdCmt = Optional.ofNullable(brdCmtMapper.read(brd_cmt_id));
		return optBrdCmt;
	}


	@Override
	public ArrayList<BrdCmt> read_replies(int brd_cmt_reply) {
		ArrayList<BrdCmt> result = brdCmtMapper.read_reply(brd_cmt_reply);
		return result;
	}

	@Override
	public int modify(int cmt_id, BrdCmt newCmt, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		
		BrdCmt brdCmt = brdCmtMapper.read(cmt_id);
		
		if (brdCmt.getMemId() == mem_id) {
			int result = brdCmtMapper.update(cmt_id, newCmt.getBrdCmtContents(),
					newCmt.getBrdCmtState());
			return result;
		} else {
			return -1;
		}
	}

	@Override
	public int remove(int id, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		BrdCmt brdCmt = brdCmtMapper.read(id);

		if (brdCmt.getMemId() == mem_id) {
			int result = brdCmtMapper.delete(id);
			if(brdCmt.getBrdCmtReply() != 0) {
				cmt_reply_minus(brdCmt.getBrdCmtReply());
			}
			return result;
		} else {
			return -1;
		}
	}

	@Override
	public int cmt_reply_count(int brd_cmt_id) {
		int result = brdCmtMapper.cmt_reply_count(brd_cmt_id);
		return result;
		
	}
	
	public int cmt_reply_minus(int brd_cmt_id) {
		int result = brdCmtMapper.cmt_reply_minus(brd_cmt_id);
		return result;
		
	}
	
	@Override
	public int getPageNum(int brd_id) {
		int pagecount = 10;
		int brdNum = brdCmtMapper.cmtcount(brd_id);
		int result = brdNum / pagecount;
		
		if (brdNum % pagecount != 0) {
			result = brdNum / pagecount + 1;
			return result;
		} else return result;
	}
	

	public Optional<BrdCmtReport> findCmtReport(int brd_id, int mem_id) {
		Optional<BrdCmtReport> optBrdReport = Optional.ofNullable( brdCmtReportRepository.findByBrdCmtIdAndMemId(brd_id, mem_id));
		return optBrdReport;
	}

	@Override
	public int report(BrdCmtReport brdCmtReport) {
		brdCmtReportRepository.save(brdCmtReport);
		
		return brdCmtMapper.report(brdCmtReport.getBrdCmtId());
	}
	














}
