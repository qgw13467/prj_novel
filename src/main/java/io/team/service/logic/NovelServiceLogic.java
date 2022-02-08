package io.team.service.logic;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.team.domain.Novel;
import io.team.jwt.JwtManager;
import io.team.mapper.NovelMapper;
import io.team.service.WriteService;

@Service
public class NovelServiceLogic implements WriteService<Novel> {

	@Autowired
	JwtManager jwtManager;

	@Autowired
	NovelMapper novelMapper;

	@Override
	public int register(Novel newNovel, String token) {
		if (newNovel.getImg_id() == 0) {
			newNovel.setImg_id(1);
		}

		int mem_id = jwtManager.getIdFromToken(token);
		if (newNovel.getMem_id() == mem_id) {
			int result = novelMapper.create(newNovel.getMem_id(), newNovel.getImg_id(), newNovel.getNv_writer(),
					newNovel.getNv_title(), newNovel.getNv_contents(), newNovel.getNv_state());
			return result;
		} else {
			return -1;
		}
	}

	@Override
	public Novel find(int id) {
		novelMapper.count_hit(id);
		return novelMapper.read(id);
	}

	@Override
	public int modify(int id, Novel newNovel, String token) {
		newNovel.setNv_id(id);

		if (newNovel.getImg_id() == 0) {
			newNovel.setImg_id(1);
		}
		Novel novel = novelMapper.read(id);
		int mem_id = jwtManager.getIdFromToken(token);
		if (novel.getMem_id() == mem_id) {
			int result = novelMapper.update(newNovel.getNv_id(), newNovel.getImg_id(), newNovel.getNv_writer(),
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

	public int review(int id, int point, String token) {
		// TODO Auto-generated method stub
		return 0;
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
