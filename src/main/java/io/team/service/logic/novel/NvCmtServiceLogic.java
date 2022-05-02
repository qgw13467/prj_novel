package io.team.service.logic.novel;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.team.Repository.Novel.NvCmtReportRepository;
import io.team.domain.BrdCmtReport;
import io.team.domain.NovelCmt;
import io.team.domain.NovelCmtReport;
import io.team.jwt.JwtManager;
import io.team.mapper.NvCmtMapper;
import io.team.service.CmtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NvCmtServiceLogic implements CmtService<NovelCmt, NovelCmtReport>{
	

	private final JwtManager jwtManager;

	private final NvCmtMapper nvCmtMapper;
	
	private final NvCmtReportRepository nvCmtReportRepository;
	
	@Override
	public int register(NovelCmt newCmt, String token) {
		
		
		if(newCmt.getNvCmtReply()!=0) {
			cmt_reply_count(newCmt.getNvCmtReply());
		}
		int result = nvCmtMapper.create(newCmt.getNvId(), newCmt.getMemId(), newCmt.getNvCmtReply(),
				newCmt.getMemNickname(), newCmt.getNvCmtContents(), newCmt.getNvCmtState());
		return result;
		
		

	}

	@Override
	public int modify(int cmt_id, NovelCmt newCmt, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		
		NovelCmt brdCmt = nvCmtMapper.read(cmt_id);
		
		if (brdCmt.getMemId() == mem_id) {
			int result = nvCmtMapper.update(cmt_id, newCmt.getNvCmtContents(),
					newCmt.getNvCmtState());
			return result;
		} else {
			return -1;
		}
	}

	
	//nvCmtState를 1로 변경
	@Override
	public int remove(int nvCmtId, String token) {
		int memId = jwtManager.getIdFromToken(token);
		NovelCmt nvCmt = nvCmtMapper.read(nvCmtId);

		if (nvCmt.getMemId() == memId) {
			nvCmt.setNvCmtState(1);
			int result = nvCmtMapper.delete(nvCmtId);
			return result;
		} else {
			return -1;
		}
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
	
	public Optional<NovelCmtReport> findCmtReport(int nv_cmt_id, int mem_id) {
		Optional<NovelCmtReport> optnovelCmtReport = Optional.ofNullable( nvCmtReportRepository.findByNvCmtIdAndMemId(nv_cmt_id, mem_id));
		return optnovelCmtReport;
	}

	@Override
	public int report(NovelCmtReport novelCmtReport) {
		nvCmtReportRepository.save(novelCmtReport);
		
		return nvCmtMapper.report(novelCmtReport.getNvCmtId());
	}



}
