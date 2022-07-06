package io.team.service.logic.novel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.team.Repository.Board.BrdCmtReportRepository;
import io.team.Repository.Novel.NvCoverMapper;
import io.team.Repository.Novel.NvCoverReportRepository;
import io.team.domain.BrdCmtReport;
import io.team.domain.NovelCover;
import io.team.domain.NovelCoverReport;
import io.team.jwt.JwtManager;
import io.team.mapper.BrdCmtMapper;
import io.team.service.WriteService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NvCoverServiceLogic implements WriteService<NovelCover> {

	
	private final NvCoverMapper nvCoverMapper;
	private final NvCoverReportRepository nvCoverReportRepository;

	//새 소설커버 등록
	@Override
	public int register(NovelCover novelCover, String token) {

		int result = 0;
		NovelCover tempNvCover = nvCoverMapper.save(novelCover);
		result= tempNvCover.getNvcId();
		
		return result;
	}
	
	//페이지로 목록가져오기
	public Page<NovelCover> findAll(Pageable pageable) {
		return nvCoverMapper.findAll(pageable);
	}
	
	public Page<NovelCover> findAll(List<Integer> novelCovers, Pageable pageable) {
		return nvCoverMapper.findAllByNvcIdIn(novelCovers, pageable);
	}
	
	//id로 소설커버 찾기
	@Override
	public NovelCover find(int id) {
		NovelCover tempCover = nvCoverMapper.findByNvcId(id);
		return tempCover;
	}
	
	public NovelCover findByNvid(int id) {
		NovelCover tempCover = nvCoverMapper.findFirstByNvId(id);
		return tempCover;
	}
	
	public int plusSubscribeCountByNvcId(int nvcId) {
		return nvCoverMapper.plusSubscribeCount(nvcId);
	}
	
	public int minusSubscribeCountByNvcId(int nvcId) {
		return nvCoverMapper.minusSubscribeCount(nvcId);
	}
	
	//수정
	@Override
	public int modify(int id, NovelCover obj, String token) {
		
		try {
			NovelCover tempCover = nvCoverMapper.save(obj);
			return tempCover.getNvcId();
		}catch (Exception e) {
			return -1;
		}
		
	}
	
	
	public Page<NovelCover> findByTitleContain(String keyword, Pageable pageable) {
		return nvCoverMapper.findByNvcTitleContaining(keyword, pageable);
	}

	@Override
	public int remove(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<NovelCover> getList(int pagenum, int rownum) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int getPageNum() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Optional<NovelCoverReport> findReport(int nvcId, int memId) {
		return nvCoverReportRepository.findByNvcIdAndMemId(nvcId, memId);
	}
	
	
	public NovelCoverReport report(NovelCoverReport novelCoverReport) {
		return nvCoverReportRepository.save(novelCoverReport);
	}

	

}
