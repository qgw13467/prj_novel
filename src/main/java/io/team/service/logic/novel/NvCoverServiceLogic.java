package io.team.service.logic.novel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.team.domain.NovelCover;
import io.team.jwt.JwtManager;
import io.team.mapper.NvCoverMapper;
import io.team.service.WriteService;

@Service
public class NvCoverServiceLogic implements WriteService<NovelCover> {

	@Autowired
	NvCoverMapper nvCoverMapper;

	//새 소설커버 등록
	@Override
	public int register(NovelCover novelCover, String token) {

		if (novelCover.getNvid() == 0) {
			novelCover.setNvid(1);
		}

		try {
			int result = 0;
			NovelCover tempNvCover = nvCoverMapper.save(novelCover);
			result= tempNvCover.getNvcid();
			
			return result;
		} catch (Exception e) {
			return -1;
			
		}
	}
	
	//페이지로 목록가져오기
	public Page<NovelCover> findAll(Pageable pageable) {
		return nvCoverMapper.findAll(pageable);
	}
	
	public Page<NovelCover> findAll(List<Integer> novelCovers, Pageable pageable) {
		return nvCoverMapper.findAllByNvcidIn(novelCovers, pageable);
	}
	
	//id로 소설커버 찾기
	@Override
	public NovelCover find(int id) {
		NovelCover tempCover = nvCoverMapper.findByNvcid(id);
		return tempCover;
	}
	
	public List<NovelCover> findByNvId(int id) {
		List<NovelCover> tempCover = nvCoverMapper.findByNvid(id);
		return tempCover;
	}

	//수정
	@Override
	public int modify(int id, NovelCover obj, String token) {
		
		try {
			NovelCover tempCover = nvCoverMapper.save(obj);
			return tempCover.getNvcid();
		}catch (Exception e) {
			return -1;
		}
		

	}

	@Override
	public int remove(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<NovelCover> getList(int pagenum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int report(int id, String token) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPageNum() {
		// TODO Auto-generated method stub
		return 0;
	}



}
