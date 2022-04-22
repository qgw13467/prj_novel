package io.team.service.logic.novel;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.team.Repository.NvCoverMapper;
import io.team.Repository.NvLinkMapper;
import io.team.domain.Novel;
import io.team.domain.NovelCover;
import io.team.domain.NovelLink;
import io.team.jwt.JwtManager;
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

		novelMapper.create(newNovel);
		return newNovel.getNvId();
	}

	public int register(Novel newNovel, String token, int parent, int titleId) {

		int mem_id = jwtManager.getIdFromToken(token);
		newNovel.setMemId(mem_id);
		novelMapper.create(newNovel);
		NovelCover novelCover = nvCoverMapper.findByNvcId(titleId);
		NovelLink novelLink = new NovelLink();
		novelLink.setNvlParents(parent);
		novelLink.setNvlChildnode(newNovel.getNvId());
		novelLink.setNvId(novelCover.getNvId());
		nvLinkMapper.save(novelLink);
		return 1;
	}

	@Override
	public Novel find(int id) {
		return novelMapper.read(id);
	}

	public Novel findInfo(int id) {
		return novelMapper.readInfo(id);
	}

	public int countCheck(int id) {
		return novelMapper.count_hit(id);
	}

	public ArrayList<NovelLink> findLinks(int nv_id) {
		return nvLinkMapper.findByNvId(nv_id);
	}

	public int findFirstNvid(int nv_id) {

		int result = 0;
		NovelLink novelLink = nvLinkMapper.findByNvlChildnode(nv_id);
		if (novelLink == null) {
			result = nv_id;
		} else {
			result = novelLink.getNvId();
		}

		return result;
	}

	@Override
	public int modify(int id, Novel newNovel, String token) {
		newNovel.setNvId(id);

		Novel novel = novelMapper.read(id);
		int mem_id = jwtManager.getIdFromToken(token);
		if (novel.getMemId() == mem_id) {
			int result = novelMapper.update(newNovel.getNvId(), newNovel.getImgUrl(), newNovel.getNvWriter(),
					newNovel.getNvTitle(), newNovel.getNvContents(), newNovel.getNvState());

			return result;

		} else {
			return -1;
		}
	}

	@Override
	public int remove(int id, String token) {
		int mem_id = jwtManager.getIdFromToken(token);
		Novel novel = novelMapper.read(id);

		if (novel.getMemId() == mem_id) {

			return novelMapper.delete(id);

		} else {

			return -1;
		}
	}

	@Override
	public ArrayList<Novel> getList(int pagenum, int rownum) {
		return novelMapper.getNovels((pagenum - 1) * rownum, rownum);
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
