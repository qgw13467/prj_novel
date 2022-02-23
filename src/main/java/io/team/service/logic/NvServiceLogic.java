package io.team.service.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.team.domain.Novel;
import io.team.domain.NovelCover;
import io.team.domain.NovelLink;
import io.team.jwt.JwtManager;
import io.team.mapper.NvCoverMapper;
import io.team.mapper.NvLinkMapper;
import io.team.mapper.NvMapper;
import io.team.mapper.NvTagMapper;
import io.team.mapper.TagMapper;
import io.team.service.WriteService;

@Service
public class NvServiceLogic implements WriteService<Novel> {

	@Autowired
	JwtManager jwtManager;
	
	@Autowired
	NvCoverMapper nvCoverMapper;
	
	@Autowired
	NvLinkMapper nvLinkMapper;
	
	@Autowired
	NvMapper novelMapper;
	
	@Autowired
	NvTagMapper nvTagMapper;
	
	@Autowired
	TagMapper tagMapper;
	
	
	@Override
	public int register(Novel newNovel, String token) {


		int mem_id = jwtManager.getIdFromToken(token);
		if (newNovel.getMem_id() == mem_id) {
			
			novelMapper.create(newNovel);
			
			return newNovel.getNv_id();
		} else {
			return -1;
		}
	}
	

	public int register(Novel newNovel, String token,int parent, int titleId) {

		
		int mem_id = jwtManager.getIdFromToken(token);
		
		if (newNovel.getMem_id() == mem_id) {
			int newNovelId = novelMapper.create(newNovel);
			
			NovelCover novelCover = nvCoverMapper.findByNvcid(titleId);
			NovelLink novelLink= new NovelLink();
			novelLink.setNvlparents(parent);
			novelLink.setNvl_childnode(newNovel.getNv_id());
			novelLink.setNvid(novelCover.getNvid());
			nvLinkMapper.save(novelLink);
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public Novel find(int id) {
		return novelMapper.read(id);
	}
	
	public Novel findInfo(int id) {
		return novelMapper.read(id);
	}
	
	public int countCheck(int id) {
		return novelMapper.count_hit(id);
	}
	
	
	public ArrayList<NovelLink> findLinks(int nv_id) {
		return nvLinkMapper.findByNvid(nv_id);
	}
	
	
	

	@Override
	public int modify(int id, Novel newNovel, String token) {
		newNovel.setNv_id(id);


		Novel novel = novelMapper.read(id);
		int mem_id = jwtManager.getIdFromToken(token);
		if (novel.getMem_id() == mem_id) {
			int result = novelMapper.update(newNovel.getNv_id(), newNovel.getImg_url(), newNovel.getNv_writer(),
					newNovel.getNv_title(), newNovel.getNv_contents(), newNovel.getNv_state());

			return result;

		} else {
			return -1;
		}
	}

	@Override
	public int remove(int id, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		Novel novel = novelMapper.read(id);

		if (novel.getMem_id() == mem_id) {

			return novelMapper.delete(id);

		} else {

			return -1;
		}
	}

	@Override
	public ArrayList<Novel> getList(int pagenum) {
		int pagecount = 10;
		return novelMapper.getNovels((pagenum - 1) * pagecount, pagecount);
	}

	public int review(int id, int point) {
		return novelMapper.review(id, point);
	}
	
	public int deleteReview(int id, int point) {
		return novelMapper.review(id, point);
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
