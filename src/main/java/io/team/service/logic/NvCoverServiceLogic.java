package io.team.service.logic;

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

	
	@Override
	public int register(NovelCover novelCover, String token) {
		if (novelCover.getImg_id() == 0) {
			novelCover.setImg_id(1);
		}
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
	
	public Page<NovelCover> findAll(Pageable pageable) {
		return nvCoverMapper.findAll(pageable);
	}
	
	@Override
	public NovelCover find(int id) {
		NovelCover tempCover = nvCoverMapper.findByNvcid(id);
		return tempCover;
	}
	

	public List<NovelCover> findByNvId(int id) {
		List<NovelCover> tempCover = nvCoverMapper.findByNvid(id);
		return tempCover;
	}

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
