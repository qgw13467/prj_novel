package io.team.mapper;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import io.team.domain.NovelCmt;


@Repository
public interface NvCmtMapper {
	int create(int nv_id, int mem_id, int nv_cmt_reply, String mem_nickname,
			String nv_cmt_contents, int nv_cmt_state);
	
	NovelCmt read(int nv_cmt_id);
	
	ArrayList<NovelCmt> read_cmts(int nv_id, int pagenum, int pagecount);
	
	ArrayList<NovelCmt> read_reply(int nv_cmt_reply);
	
	int update(int nv_cmt_id, String nv_cmt_contents, int nv_cmt_state);

	int delete(int nv_cmt_id);
	
	int like(int nv_cmt_id);
	int likeMinus(int nv_cmt_id);
	
	int dislike(int nv_cmt_id);
	int dislikeMinus(int nv_cmt_id);
	
	int report(int nv_cmt_id);
	
	int cmtcount(int nv_id);
	
	int cmt_reply_count(int nv_cmt_id);
}
