package io.team.mapper;

import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import io.team.domain.Novel;

@Repository
public interface NvMapper {
	
	int create(Novel novel);

	Novel read(int nv_id);
	
	Novel readInfo(int nv_id);

	int update(int nv_id, String img_url, String nv_writer, String nv_title, String nv_contents, int nv_state);

	int delete(int nv_id);

	int count_hit(int nv_id);

	ArrayList<Novel> getNovels(int pagenum, int pagecount);

	int nvcount();
//////////////////////
	int review(int nv_id, int point);
	
	int deletReview(int nv_id, int point);

	int getNextNovel();

	int report(int nv_id, int mem_id);
	
	int plusCmtCount(int nv_id);
	int minusCmtCount(int nv_id);
}
